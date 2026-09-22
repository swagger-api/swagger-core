package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/pets")
public class QueryMethodResource {

    @QUERY
    @Path("/search")
    @Produces("application/json")
    @Operation(operationId = "searchPets", summary = "Search pets")
    public Response searchPets() {
        return Response.ok().build();
    }
}
