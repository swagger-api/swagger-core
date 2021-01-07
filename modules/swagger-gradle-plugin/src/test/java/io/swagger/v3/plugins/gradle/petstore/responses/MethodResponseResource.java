package io.swagger.v3.plugins.gradle.petstore.responses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.plugins.gradle.resources.exception.NotFoundException;
import io.swagger.v3.plugins.gradle.resources.model.Pet;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * Resource with a Response at Method Level
 */
public class MethodResponseResource {
    @GET
    @Path("/responseinmethod")
    @Operation(summary = "Find pets",
            description = "Returns the Pets")
    @ApiResponse(responseCode = "200", description = "Status OK")
    public Response getPets() throws NotFoundException {
        return Response.ok().entity(new Pet()).build();
    }
}
