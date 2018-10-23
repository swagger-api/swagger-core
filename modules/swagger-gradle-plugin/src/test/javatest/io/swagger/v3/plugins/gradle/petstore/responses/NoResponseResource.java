package io.swagger.v3.plugins.gradle.petstore.responses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.plugins.gradle.resources.exception.NotFoundException;
import io.swagger.v3.plugins.gradle.resources.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
