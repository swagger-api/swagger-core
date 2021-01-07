package io.swagger.v3.plugin.maven.petstore.petstore.operation;

import io.swagger.v3.plugin.maven.resources.model.Pet;
import io.swagger.v3.plugin.maven.resources.model.User;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 * Resource With a Hidden Operation
 */
public class HiddenOperationResource {
    @Path("/hiddenbyflag")
    @GET
    @Operation(operationId = "Pets", description = "Pets Example", hidden = true)
    public Pet getPet() {
        return new Pet();
    }

    @Path("/hiddenbyannotation")
    @GET
    @Operation(operationId = "Users", description = "Users Example")
    @Hidden
    public User getUser() {
        return new User();
    }
}
