package io.swagger.v3.plugins.gradle.petstore.operation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.plugins.gradle.resources.model.Pet;

import javax.ws.rs.core.Response;

/**
 * SubResource
 */

public class SubResource implements InterfaceResource {
    @Override
    @Operation(summary = "Find pet by ID Operation in SubResource",
            description = "Returns a pet in SubResource"
    )
    public Response getPetById(final Long petId) {
        return Response.ok().entity(new Pet()).build();
    }
}
