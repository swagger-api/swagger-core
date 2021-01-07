package io.swagger.v3.plugins.gradle.petstore.parameter;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

/**
 * Class with a multiple not annotated parameter.
 */
public class MultipleNotAnnotatedParameter {
    @POST
    @Path("/multiplenoannotatedparameter")
    @Operation(operationId = "create User")
    public void createUser(final String id, final String name) {

    }
}
