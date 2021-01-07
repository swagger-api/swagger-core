package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

public class SimpleRequestBodyResource {

    @GET
    @Path("/")
    @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with a payload complex input object",
            operationId = "sendPayload",
            deprecated = true,
            requestBody = @RequestBody(description = "Test RequestBody")
    )
    public void sendPayload(final User user) {
    }

}
