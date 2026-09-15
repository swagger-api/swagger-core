package io.swagger.v3.jaxrs2.it.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/cars")
@Tag(name = "cars")
@Produces("application/json")
@Consumes("application/json")
public class CarResource {

    private static class View {

        interface Summary {
        }

        interface Detail extends View.Summary {
        }

        interface Sale {
        }
    }

    private static class Car {

        @JsonView(View.Summary.class)
        @JsonProperty("manufacture")
        private String made = "Honda";

        @JsonView({View.Summary.class, View.Detail.class})
        private String model = "Accord Hybrid";

        @JsonView({View.Detail.class})
        @JsonProperty
        private List<Tire> tires = Arrays.asList(new Tire());

        @JsonView({View.Sale.class})
        @JsonProperty
        private int price = 40000;

        // always in
        private String color = "White";

        public String getColor() {
            return color;
        }
    }

    private static class Tire {
        @JsonView(View.Summary.class)
        @JsonProperty("brand")
        private String made = "Michelin";

        @JsonView(View.Detail.class)
        @JsonProperty
        private String condition = "new";
    }

    @GET
    @Path("/summary")
    @JsonView({View.Summary.class})
    @Operation(description = "Return car summaries",
            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Car.class)))))
    public Response getSummaries() {
        return Response.ok(Arrays.asList(new Car())).build();
    }

    @GET
    @Path("/detail")
    @JsonView({View.Detail.class})
    @Operation(description = "Return car detail",
            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Car.class)))))
    public List<Car> getDetails() {
        return Arrays.asList(new Car());
    }

    @GET
    @Path("/sale")
    @JsonView({View.Summary.class, View.Sale.class})
    public List<Car> getSaleSummaries() {
        return Arrays.asList(new Car());
    }

    @GET
    @Path("/all")
    @Operation(description = "Return whole car",
            responses = @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Car.class)))))
    public List<Car> getAll() {
        return Arrays.asList(new Car());
    }

}