package io.swagger.v3.jaxrs2;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.Assert;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

public class JsonViewTest {

    private static class View {

        interface Summary {

        }

        interface Detail extends Summary {

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

    @Path("/")
    private static class CarSummaryApi {

        @GET
        @Path("/cars/summary")
        @JsonView({View.Summary.class})
        public List<Car> getSummaries() {
            return Arrays.asList(new Car());
        }
    }

    @Path("/")
    private static class CarDetailApi {

        @GET
        @Path("/cars/detail")
        @JsonView({View.Detail.class})
        @Operation(description = "Return car detail",
                responses = @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema (implementation = Car.class)))))
        public Response getDetails() {
            return Response.ok(Arrays.asList(new Car())).build();
        }
    }

    @Path("/")
    private static class CarSaleSummaryApi {

        @GET
        @Path("/cars/sale")
        @JsonView({View.Summary.class, View.Sale.class})
        public List<Car> getSaleSummaries() {
            return Arrays.asList(new Car());
        }
    }

    @Path("/")
    private static class CarApi {

        @GET
        @Path("/cars")
        public List<Car> getCars() {
            return Arrays.asList(new Car());
        }
    }

    @Path("/ignore")
    private static class CarIgnoreApi {

        @GET
        @Path("/cars")
        @JsonView({View.Summary.class, View.Sale.class})
        @Operation(ignoreJsonView = true)
        public List<Car> getCars() {
            return Arrays.asList(new Car());
        }
    }

    @Test(description = "check awareness of JsonView")
    public void shouldSerializeTypeParameter() throws JsonProcessingException {

        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(CarSummaryApi.class);
        String openApiJson = Json.mapper().writeValueAsString(openAPI);
        Assert.assertTrue(openApiJson.contains("manufacture"));
        Assert.assertTrue(openApiJson.contains("model"));
        Assert.assertTrue(openApiJson.contains("color"));
        Assert.assertFalse(openApiJson.contains("tires"));
        Assert.assertFalse(openApiJson.contains("made"));
        Assert.assertFalse(openApiJson.contains("condition"));

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(CarDetailApi.class);
        openApiJson = Json.mapper().writeValueAsString(openAPI);
        Assert.assertTrue(openApiJson.contains("manufacture"));
        Assert.assertTrue(openApiJson.contains("model"));
        Assert.assertTrue(openApiJson.contains("color"));
        Assert.assertTrue(openApiJson.contains("tires"));
        Assert.assertTrue(openApiJson.contains("brand"));
        Assert.assertTrue(openApiJson.contains("condition"));
        Assert.assertTrue(openApiJson.contains("Car_Detail"));

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(CarSaleSummaryApi.class);
        openApiJson = Json.mapper().writeValueAsString(openAPI);
        Yaml.prettyPrint(openAPI);
        Assert.assertTrue(openApiJson.contains("manufacture"));
        Assert.assertTrue(openApiJson.contains("model"));
        Assert.assertTrue(openApiJson.contains("color"));
        Assert.assertTrue(openApiJson.contains("price"));
        Assert.assertFalse(openApiJson.contains("tires"));
        Assert.assertFalse(openApiJson.contains("made"));
        Assert.assertFalse(openApiJson.contains("condition"));

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(CarApi.class);
        openApiJson = Json.mapper().writeValueAsString(openAPI);
        Assert.assertTrue(openApiJson.contains("manufacture"));
        Assert.assertTrue(openApiJson.contains("model"));
        Assert.assertTrue(openApiJson.contains("color"));
        Assert.assertTrue(openApiJson.contains("price"));
        Assert.assertTrue(openApiJson.contains("tires"));
        Assert.assertFalse(openApiJson.contains("made"));
        Assert.assertTrue(openApiJson.contains("condition"));

        reader = new Reader(new OpenAPI());
        openAPI = reader.read(CarIgnoreApi.class);
        openApiJson = Json.mapper().writeValueAsString(openAPI);
        Assert.assertTrue(openApiJson.contains("manufacture"));
        Assert.assertTrue(openApiJson.contains("model"));
        Assert.assertTrue(openApiJson.contains("color"));
        Assert.assertTrue(openApiJson.contains("price"));
        Assert.assertTrue(openApiJson.contains("tires"));
        Assert.assertFalse(openApiJson.contains("made"));
        Assert.assertTrue(openApiJson.contains("condition"));
    }

}