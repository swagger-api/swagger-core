package io.swagger.resources;

import io.swagger.annotations.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Api
@Path("/")
public class ResourceWithExternalDocs {

    @GET
    @Path("/testString")
    @ApiOperation("Test operation without externalDocs")
    public void testStringGet() {
    }

    @POST
    @Path("/testString")
    @ExternalDocs(value = "Test Description", url = "https://swagger.io/")
    @ApiOperation("Test operation with externalDocs")
    public void testStringPost() {
    }
}
