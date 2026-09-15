package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.data.UserData;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/user")
@Produces({"application/json", "application/xml"})
@Consumes({"application/json", "application/xml"})
public class SimpleUserResource {
    static UserData userData = new UserData();

    @POST
    @Operation(summary = "Create user",
            description = "This can only be done by the logged in user.")
    public Response createUser(
            @Parameter(description = "Created user object", required = true) User user) {
        userData.addUser(user);
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/createUserWithReturnType")
    @Operation(summary = "Create user with return type",
            description = "This can only be done by the logged in user.")
    public User createUserWithReturnType(
            @Parameter(description = "Created user object", required = true) User user) {
        userData.addUser(user);
        return null;
    }

    @POST
    @Path("/createUserWithResponseAnnotation")
    @Operation(summary = "Create user with response annotation",
            description = "This can only be done by the logged in user.")
    @ApiResponse(description = "aaa", responseCode = "200", content = {@Content(schema = @Schema(implementation = User.class))})
    public User createUserWithResponseAnnotation(
            @Parameter(description = "Created user object", required = true) User user) {
        userData.addUser(user);
        return null;
    }

    @POST
    @Path("/createUserWithReturnTypeAndResponseAnnotation")
    @Operation(summary = "Create user with return type and response annotation",
            description = "This can only be done by the logged in user.")
    @ApiResponse(description = "aaa", responseCode = "200")
    public User createUserWithReturnTypeAndResponseAnnotation(
            @Parameter(description = "Created user object", required = true) User user) {
        userData.addUser(user);
        return null;
    }
}
