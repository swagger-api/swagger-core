package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

public class SingleExampleResource {

    @POST
    @Path("/test1")
    public Response test1(
            @RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"foo\" : \"foo\", \"bar\" : \"bar\"}")
                    )
            ) final User user) {
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/test2")
    @Operation(requestBody = @RequestBody(
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = User.class),
                    examples = @ExampleObject(value = "{\"foo\" : \"foo\", \"bar\" : \"bar\"}")
            )
    ))
    public Response test2(final User user) {
        return Response.ok().entity("").build();
    }
}
