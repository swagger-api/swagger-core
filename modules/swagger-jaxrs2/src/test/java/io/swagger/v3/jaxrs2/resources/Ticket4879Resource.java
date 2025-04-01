package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Size;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
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

    public static class DefaultClass {
        @Schema(defaultValue = "true")
        public Boolean name;
    }
}
