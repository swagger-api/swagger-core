package io.swagger.v3.plugin.maven.petstore.petstore.operation;

import io.swagger.v3.plugin.maven.resources.exception.NotFoundException;
import io.swagger.v3.plugin.maven.resources.model.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/**
 * Resource with Operations Examples
 */
public class FullyAnnotatedOperationResource {
    @GET
    @Path("/fullyannotatedoperation/{petId}")
    @Operation(summary = "Find pet by ID",
            description = "Returns a pet when 0 < ID <= 10.  ID > 10 or non integers will simulate API error conditions",
            operationId = "petId",
            responses = {
                    @ApiResponse(
                            description = "The pet", content = @Content(
                            schema = @Schema(implementation = Pet.class)
                    )),
                    @ApiResponse(responseCode = "400", description = "Invalid ID supplied"),
                    @ApiResponse(responseCode = "404", description = "Pet not found")
            })
    public Response getPetById(
            @Parameter(description = "ID of pet that needs to be fetched", required = true)
            @PathParam("petId")final Long petId) throws NotFoundException {
        return Response.ok().entity(new Pet()).build();
    }
}
