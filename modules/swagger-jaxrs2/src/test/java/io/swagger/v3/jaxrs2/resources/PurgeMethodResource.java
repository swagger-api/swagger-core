package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/cache")
public class PurgeMethodResource {

    @PURGE
    @Path("/entries")
    @Produces("application/json")
    @Operation(operationId = "purgeCache", summary = "Purge cache entries")
    public Response purgeCache() {
        return Response.ok().build();
    }
}
