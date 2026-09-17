package io.swagger.v3.jaxrs2.petstore.tags;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * Resource with a Tag at Operation Level
 */
public class TagOperationResource {

    @GET
    @Path("/tagoperation")
    @Operation(tags = {"Example Tag", "Second Tag"})
    public Response getTags() {
        return Response.ok().entity("ok").build();
    }
}
