package io.swagger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.jaxrs.Reader;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;
import io.swagger.util.Json;

import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.testng.Assert;
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

    @Api
    @Path("/")
    private static class CarSummaryApi {

        @GET
        @Path("/cars/summary")
        @JsonView({View.Summary.class})
        public List<Car> getSummaries() {
            return Arrays.asList(new Car());
        }
    }

    @Api
    @Path("/")
    private static class CarDetailApi {

        @GET
        @Path("/cars/detail")
        @JsonView({View.Detail.class})
        @ApiOperation(value = "Return car detail", response = Car.class, responseContainer = "List")
        public Response getDetails() {
            return Response.ok(Arrays.asList(new Car())).build();
        }
    }

    @Api
    @Path("/")
    private static class CarSaleSummaryApi {

        @GET
        @Path("/cars/sale")
        @JsonView({View.Summary.class, View.Sale.class})
        public List<Car> getSaleSummaries() {
            return Arrays.asList(new Car());
        }
    }

    @Api
    @Path("/")
    private static class CarApi {

        @GET
        @Path("/cars")
        public List<Car> getCars() {
            return Arrays.asList(new Car());
        }
    }

    @Api
    @Path("/ignore")
    private static class CarIgnoreApi {

        @GET
        @Path("/cars")
        @JsonView({View.Summary.class, View.Sale.class})
        @ApiOperation(value = "getCars", ignoreJsonView = true)
        public List<Car> getCars() {
            return Arrays.asList(new Car());
        }
    }

    @Test(description = "check awareness of JsonView")
    public void shouldSerializeTypeParameter() throws JsonProcessingException {
        Swagger swagger = getSwagger(CarSummaryApi.class);
        String swaggerJson = Json.mapper().writeValueAsString(swagger);
        Assert.assertTrue(swaggerJson.contains("manufacture"));
        Assert.assertTrue(swaggerJson.contains("model"));
        Assert.assertTrue(swaggerJson.contains("color"));
        Assert.assertFalse(swaggerJson.contains("tires"));
        Assert.assertFalse(swaggerJson.contains("made"));
        Assert.assertFalse(swaggerJson.contains("condition"));

        swagger = getSwagger(CarDetailApi.class);
        swaggerJson = Json.mapper().writeValueAsString(swagger);
        Assert.assertTrue(swaggerJson.contains("manufacture"));
        Assert.assertTrue(swaggerJson.contains("model"));
        Assert.assertTrue(swaggerJson.contains("color"));
        Assert.assertTrue(swaggerJson.contains("tires"));
        Assert.assertTrue(swaggerJson.contains("brand"));
        Assert.assertTrue(swaggerJson.contains("condition"));
        Assert.assertTrue(swaggerJson.contains(Car.class.getName()+"_"+ View.Detail.class.getName()));

        swagger = getSwagger(CarSaleSummaryApi.class);
        swaggerJson = Json.mapper().writeValueAsString(swagger);
        Assert.assertTrue(swaggerJson.contains("manufacture"));
        Assert.assertTrue(swaggerJson.contains("model"));
        Assert.assertTrue(swaggerJson.contains("color"));
        Assert.assertTrue(swaggerJson.contains("price"));
        Assert.assertFalse(swaggerJson.contains("tires"));
        Assert.assertFalse(swaggerJson.contains("made"));
        Assert.assertFalse(swaggerJson.contains("condition"));

        swagger = getSwagger(CarApi.class);
        swaggerJson = Json.mapper().writeValueAsString(swagger);
        Assert.assertTrue(swaggerJson.contains("manufacture"));
        Assert.assertTrue(swaggerJson.contains("model"));
        Assert.assertTrue(swaggerJson.contains("color"));
        Assert.assertTrue(swaggerJson.contains("price"));
        Assert.assertTrue(swaggerJson.contains("tires"));
        Assert.assertFalse(swaggerJson.contains("made"));
        Assert.assertTrue(swaggerJson.contains("condition"));

        swagger = getSwagger(CarIgnoreApi.class);
        swaggerJson = Json.mapper().writeValueAsString(swagger);
        Assert.assertTrue(swaggerJson.contains("manufacture"));
        Assert.assertTrue(swaggerJson.contains("model"));
        Assert.assertTrue(swaggerJson.contains("color"));
        Assert.assertTrue(swaggerJson.contains("price"));
        Assert.assertTrue(swaggerJson.contains("tires"));
        Assert.assertFalse(swaggerJson.contains("made"));
        Assert.assertTrue(swaggerJson.contains("condition"));
    }

    private Swagger getSwagger(Class<?> cls) {
        return new Reader(new Swagger()).read(cls);
    }

}