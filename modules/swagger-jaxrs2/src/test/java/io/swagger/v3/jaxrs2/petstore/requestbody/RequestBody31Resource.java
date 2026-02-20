package io.swagger.v3.jaxrs2.petstore.requestbody;

import io.swagger.v3.jaxrs2.petstore.parameter.Parameters31Resource;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Resource with RequestBody examples
 */
public class RequestBody31Resource {

    @GET
    @Path("/methodWithRequestBodyWithoutContentWithoutImplementation")
    @Operation(summary = "Create user",
            description = "This can only be done by the logged in user.")
    public Response methodWithRequestBodyWithoutContentWithoutImplementation(
            @RequestBody(description = "Created user object", required = true,
                    content = @Content(
                            schema = @Schema(name = "User",
                                    description = "User description",
                                    example = "User Description",
                                    required = true,
                                    types = { "string", "number", "object" },
                                    exclusiveMaximumValue = 100,
                                    exclusiveMinimumValue = 1,
                                    $schema = "parameter $schema",
                                    $anchor = "parameter $anchor",
                                    _if = Object.class,
                                    _else = Object.class,
                                    then = Object.class,
                                    unevaluatedProperties = Object.class
                            )))
            final User user) {
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/methodWithRequestBodyAndTwoParameters")
    @Operation(summary = "Create user",
            description = "This can only be done by the logged in user.")
    public Response methodWithRequestBodyAndTwoParameters(
            @RequestBody(description = "Created user object", required = true,
                    content = @Content(
                            schema = @Schema(implementation = User.class))) final User user,
            @QueryParam("name") final String name, @QueryParam("code") final String code) {
        return Response.ok().entity("").build();
    }
}
