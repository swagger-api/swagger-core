package io.swagger.v3.jaxrs2.petstore.parameter;

import io.swagger.v3.oas.annotations.Operation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

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
