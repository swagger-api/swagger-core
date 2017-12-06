package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * RequestBody resource class
 * Created by rafaellopez on 12/6/17.
 */
public class RequestBodyResource {
    @GET
    @Path("/users")
    @Operation(reqquestBody = @RequestBody(description = "RequestBody", required = true,
            content = @Content(schema = @Schema(implementation = User.class))))
    public User getUser(String id) {
        return new User();
    }
}
