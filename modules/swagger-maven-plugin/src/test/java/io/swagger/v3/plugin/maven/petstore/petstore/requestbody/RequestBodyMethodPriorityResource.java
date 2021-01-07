package io.swagger.v3.plugin.maven.petstore.petstore.requestbody;

import io.swagger.v3.plugin.maven.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * Resource with RequestBody inside Operation and another in Method
 */
public class RequestBodyMethodPriorityResource {
    @POST
    @Path("/requestbodymethodpriority")
    @Operation(summary = "Create user",
            description = "This can only be done by the logged in user.",
            requestBody = @RequestBody(description = "Inside Operation"))
    @RequestBody(description = "Created user object on Method", required = true,
            content = @Content(
                    schema = @Schema(implementation = User.class)))
    public Response methodWithRequestBodyAndTwoParameters(final User user) {
        return Response.ok().entity("").build();
    }
}
