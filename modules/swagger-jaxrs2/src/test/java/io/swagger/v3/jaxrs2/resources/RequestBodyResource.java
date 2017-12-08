package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * RequestBody resource class
 * Created by rafaellopez on 12/6/17.
 */
@Path("/user")
public class RequestBodyResource {
    @GET
    @Operation(requestBody = @RequestBody(description = "RequestBody on Annotation", required = true,
            content = @Content(schema = @Schema(implementation = User.class))))
    public User getUser() {
        return new User();
    }

    @POST
    @Operation(summary = "Create user",
            description = "This can only be done by the logged in user.",
            requestBody = @RequestBody(description = "RequestBody on Annotation", required = true,
                    content = @Content(schema = @Schema(implementation = User.class))))
    public Response createUser(
            @Parameter(description = "Request Body in Parameter", required = true) User user) {
        return Response.ok().entity("").build();
    }
}
