package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


public class TagsResource {

    @GET
    @Path("/")
    @Operation(tags = {"Example Tag", "Second Tag"})
    public Response getTags() {
        return Response.ok().entity("ok").build();
    }
}
