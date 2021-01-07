package io.swagger.v3.jaxrs2.petstore.responses;

import io.swagger.v3.jaxrs2.resources.exception.NotFoundException;
import io.swagger.v3.jaxrs2.resources.model.Pet;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * Resource with a Response at Method Level
 */
public class NoResponseResource {
    @GET
    @Path("/noresponse")
    @Operation(summary = "Find pets",
            description = "Returns the Pets")
    public User getPets() throws NotFoundException {
        return new User();
    }
}
