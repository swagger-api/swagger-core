package io.swagger.v3.plugins.gradle.petstore.parameter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.plugins.gradle.resources.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Class with a single parameter annotated with jaxrs.
 */
public class SingleJaxRSAnnotatedParameter {
    @GET
    @Path("/singlejaxrsannotatedparameter")
    @Operation(operationId = "create User")
    public User findUser(@QueryParam("id") final String id) {
        return new User();
    }
}
