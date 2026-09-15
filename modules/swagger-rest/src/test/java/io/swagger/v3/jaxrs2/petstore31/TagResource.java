package io.swagger.v3.jaxrs2.petstore31;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/tag")
public class TagResource {
    @GET
    @Path("/tag")
    public SimpleTag getTag(@RequestBody SimpleCategory category) {
        return null;
    }
}
