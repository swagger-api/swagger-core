package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

public class RefParameterResource {

    @GET
    @Path("/")
    @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with a payload complex input object",
            operationId = "sendPayload",
            deprecated = true
    )
    public void sendPayload(@Parameter(ref = "id") @QueryParam("id") final int id) {
    }

}
