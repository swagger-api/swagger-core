package io.swagger.v3.plugin.maven.petstore.petstore.responses;

import io.swagger.v3.plugin.maven.resources.exception.NotFoundException;
import io.swagger.v3.plugin.maven.resources.model.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Resource with some responses
 */
public class ComplexResponseResource {
    @GET
    @Path("/complexresponse")
    @Operation(summary = "Find pets",
            description = "Returns the Pets",
            responses = {
                    @ApiResponse(description = "Response inside Operation", responseCode = "200",
                            content = @Content(schema =
                            @Schema(implementation = Pet.class))),
                    @ApiResponse(description = "Default Pet",
                            content = @Content(schema =
                            @Schema(name = "Default Pet", description = "Default Pet",
                            required = true, example = "New Pet")))
            })
    @ApiResponse(responseCode = "404", description = "Couldn't find pet")
    public Pet getPets() throws NotFoundException {
        return new Pet();
    }
}
