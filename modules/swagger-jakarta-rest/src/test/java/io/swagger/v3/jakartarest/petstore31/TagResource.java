package io.swagger.v3.jakartarest.petstore31;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/tag")
public class TagResource {
    @GET
    @Path("/tag")
    public SimpleTag getTag(@RequestBody SimpleCategory category) {
        return null;
    }
}
