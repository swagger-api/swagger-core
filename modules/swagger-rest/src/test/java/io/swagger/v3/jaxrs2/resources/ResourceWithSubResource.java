package io.swagger.v3.jaxrs2.resources;


import io.swagger.v3.jaxrs2.resources.model.Pet;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Path;

@Path("/employees")
public class ResourceWithSubResource {
    @Operation(description = "gets all employees",
            tags = "Employees")
    @ApiResponse(description = "200", responseCode = "200",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Pet.class))))
    @Path("/{id}")
    public SubResource getTest() {
        return new SubResource();
    }

    @Path("noPath")
    @Operation(description = "Returns sub-resource without @Path")
    public NoPathSubResource getNoPathTest() {
        return new NoPathSubResource();
    }
}