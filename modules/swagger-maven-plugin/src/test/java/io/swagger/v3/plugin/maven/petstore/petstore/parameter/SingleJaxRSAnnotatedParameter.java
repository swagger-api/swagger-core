package io.swagger.v3.plugin.maven.petstore.petstore.parameter;

import io.swagger.v3.plugin.maven.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;

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
