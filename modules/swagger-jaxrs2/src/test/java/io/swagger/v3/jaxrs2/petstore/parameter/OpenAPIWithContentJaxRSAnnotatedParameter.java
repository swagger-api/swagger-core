package io.swagger.v3.jaxrs2.petstore.parameter;

import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Class with a single parameter annotated with jaxrs and open api annotation.
 */
public class OpenAPIWithContentJaxRSAnnotatedParameter {
    @GET
    @Path("/openapiwithcontentjaxrsannotatedparameter")
    @Operation(operationId = "create User")
    public User findUser(@Parameter(description = "idParam", content =
    @Content(schema = @Schema(description = "Id Schema Definition", required = true, name = "id")))
                         @QueryParam("id") final String id) {
        return new User();
    }
}
