package io.swagger.v3.plugin.maven.petstore.petstore.operation;

import io.swagger.v3.plugin.maven.resources.model.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

/**
 * Resource with Complete Operations Examples
 */
public class OperationResource implements InterfaceResource {
    @Override
    @Operation(summary = "Find pet by ID Operation in SubResource",
            description = "Returns a pet in SubResource"
    )
    public Response getPetById(final Long petId) {
        return Response.ok().entity(new Pet()).build();
    }

    @GET
    @Path("/operationsresource")
    @Operation(summary = "Find pet by ID",
            description = "combinatedfullyannotatedoperation/{petId}",
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
            @QueryParam("petId") final Long petId, final String message) throws NotFoundException {
        return Response.ok().entity(new Pet()).build();
    }

    @Path("/operationsresource")
    @POST
    public String getUser(final String id) {
        return new String();
    }

    @Path("/operationsresource")
    @PUT
    @Operation(operationId = "combinated sameOperationName",
            description = "combinatedsameOperationName")
    public String getPerson() {
        return new String();
    }

    @Path("/operationsresource")
    @HEAD
    @Operation(operationId = "combinatedsameOperationNameDuplicated",
            description = "combinatedsameOperationNameDuplicated")
    public String getPerson(final String id) {
        return new String();
    }

    @Path("/operationsresource2")
    @GET
    public String getUser() {
        return new String();
    }
}
