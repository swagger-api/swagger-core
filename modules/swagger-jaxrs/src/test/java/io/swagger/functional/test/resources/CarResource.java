package io.swagger.functional.test.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.Arrays;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/cars")
@Api(tags = "cars", description = "Car operations")
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
    @ApiOperation(value = "Return car summaries", response = Car.class, consumes = "List")
    public Response getSummaries() {
        return Response.ok(Arrays.asList(new Car())).build();
    }

    @GET
    @Path("/detail")
    @JsonView({View.Detail.class})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return car detail", response = Car.class, responseContainer = "List")
    })
    @ApiOperation(value = "Return car detail")
    public List<Car> getDetails() {
        return Arrays.asList(new Car());
    }

    @GET
    @Path("/sale")
    @JsonView({View.Summary.class, View.Sale.class})
    @ApiOperation(value = "Return car sale summary")
    public List<Car> getSaleSummaries() {
        return Arrays.asList(new Car());
    }

    @GET
    @Path("/all")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Return whole car", response = Car.class, responseContainer = "List")
    })
    @ApiOperation(value = "Return whole car")
    public List<Car> getAll() {
        return Arrays.asList(new Car());
    }

}
