package io.swagger.resources;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Api
@Path("/")
public class ApiOperationResource {
    @GET
    @Path("/users")
    @Uber(description = "Test Uber Description")
    public String getResponse() {
        return "Response";
    }
}
