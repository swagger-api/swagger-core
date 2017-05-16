package io.swagger.jaxrs2.resources;


import io.swagger.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by RafaelLopez on 5/16/17.
 */
public class ApiConsumesProducesResource {

    @GET
    @Path("/")
    @Operation(description = "Get object by ID")
    public Response noConsumesProduces() {
        return Response.ok().entity("ok").build();
    }
}
