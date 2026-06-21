package io.swagger.v3.jakartarest.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/sample")
public interface ResponseReturnTypeResource {

    @GET
    @Path("/{id}")
    @Operation(summary = "Find by id", description = "Find by id operation")
    @ApiResponse(responseCode = "200", description = "Ok", useReturnTypeSchema = true)
    @ApiResponse(responseCode = "201",
            description = "201",
            useReturnTypeSchema = true,
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "204",
            description = "No Content",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)))
    TestDTO find(@Parameter(description = "ID") @PathParam("id") Integer id);

    @GET
    @Path("/{id}/default")
    @Operation(summary = "Find by id (default)", description = "Find by id operation (default)")
    TestDTO findDefault(@Parameter(description = "ID") @PathParam("id") Integer id);

    class TestDTO {
        public String foo;
    }
}
