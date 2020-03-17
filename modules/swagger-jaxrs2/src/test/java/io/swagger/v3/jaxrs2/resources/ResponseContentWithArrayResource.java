package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

public class ResponseContentWithArrayResource {

    @Operation(
            summary = "Get a list of users",
            description = "Get a list of users registered in the system",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "The response for the user request",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class))
                            )
                    })
            }
    )
    @GET
    @SecurityRequirement(name = "JWT")
    @Path("/user")
    public List<User> getUsers() {
        return null;
    }

    class User {
        public String foo;

        @ArraySchema(arraySchema = @Schema(required = true), schema = @Schema(type = "string"))
        public List<String> issue3438;
    }
}
