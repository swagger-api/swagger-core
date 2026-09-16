package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

public class SubResource {
    @GET
    @Operation(description = "gets an object by ID",
            tags = "Employees")
    @ApiResponse(description = "200", responseCode = "200",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Pet.class))))
    public void getAllEmployees() {
        return;
    }

    @Operation(description = "gets an object by ID",
            tags = "Employees")
    @ApiResponse(description = "200", responseCode = "200",
            content = @Content(schema = @Schema(implementation = Pet.class)))
    @GET
    @Path("{id}")
    public Pet getSubresourceOperation(@PathParam("id") Long userId) {
        return null;
    }
}