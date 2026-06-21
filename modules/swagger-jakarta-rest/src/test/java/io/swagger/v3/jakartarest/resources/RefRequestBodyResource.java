package io.swagger.v3.jakartarest.resources;

import io.swagger.v3.jakartarest.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

public class RefRequestBodyResource {

    @GET
    @Path("/")
    @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with a payload complex input object",
            operationId = "sendPayload",
            deprecated = true,
            requestBody = @RequestBody(ref = "User")
    )
    public void sendPayload(final User user) {
    }

}
