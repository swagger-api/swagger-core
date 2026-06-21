package io.swagger.v3.jakartarest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Size;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.util.List;

@Path("/test")
public class Ticket4879Resource {

    @PUT
    @Path("/test")
    public void test(DefaultClass defaultClass) {}

    @GET
    @Path("/testDefaultValueAnnotation")
    public void testDefault(
            @DefaultValue(value = "true") @QueryParam(value = "myBool") Boolean myBool,
            @DefaultValue(value = "1") @QueryParam(value = "myInt") Integer myInt) {
    }

    @GET
    @Path("/testsize")
    public void testSize(@Size(min = 1, max = 100) List<String> myList) {}

    @GET
    @Path("/teststringsize")
    public void testStringSize(@Size(min = 1, max = 50) String myString) {}

    public static class DefaultClass {
        @Schema(defaultValue = "true")
        public Boolean name;
    }
}
