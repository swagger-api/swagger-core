package io.swagger.v3.plugin.maven.petstore.petstore.parameter;

import io.swagger.v3.plugin.maven.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Class with a single parameter annotated with jaxrs and open api annotation.
 */
public class OpenAPIWithImplementationJaxRSAnnotatedParameter {
    @GET
    @Path("/openapiwithimplementationjaxrsannotatedparameter")
    @Operation(operationId = "create User")
    public User findUser(@Parameter(description = "idParam", schema = @Schema(implementation = User.class))
                         @QueryParam("id") final String id) {
        return new User();
    }
}
