/**
 * Copyright 2016 SmartBear Software
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.data.UserData;
import io.swagger.v3.jaxrs2.resources.exception.ApiException;
import io.swagger.v3.jaxrs2.resources.exception.NotFoundException;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/user")
@Produces({"application/json", "application/xml"})
public class UserResource {
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
    @Path("/createWithArray")
    @Operation(summary = "Creates list of users with given input array")
    public Response createUsersWithArrayInput(@Parameter(description = "List of user object", required = true) User[] users) {
        for (User user : users) {
            userData.addUser(user);
        }
        return Response.ok().entity("").build();
    }

    @POST
    @Path("/createWithList")
    @Operation(summary = "Creates list of users with given input array")
    public Response createUsersWithListInput(@Parameter(description = "List of user object", required = true) java.util.List<User> users) {
        for (User user : users) {
            userData.addUser(user);
        }
        return Response.ok().entity("").build();
    }

    @PUT
    @Path("/{username}")
    @Operation(summary = "Updated user",
            description = "This can only be done by the logged in user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "user updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid user supplied"),
                    @ApiResponse(responseCode = "404", description = "User not found")})
    public Response updateUser(
            @Parameter(description = "name that need to be deleted", required = true, examples = {
                    @ExampleObject(name = "example1", value = "example1",
                            summary = "Summary example 1", externalValue = "external value 1"),
                    @ExampleObject(name = "example2", value = "example2",
                            summary = "Summary example 2", externalValue = "external value 2")
            }) @PathParam("username") String username,
            @Parameter(description = "Updated user object", required = true) User user) {
        userData.addUser(user);
        return Response.ok().entity("").build();
    }

    @DELETE
    @Path("/{username}")
    @Operation(summary = "Delete user",
            description = "This can only be done by the logged in user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "user deteled"),
                    @ApiResponse(responseCode = "400", description = "Invalid username supplied"),
                    @ApiResponse(responseCode = "404", description = "User not found")})
    public Response deleteUser(
            @Parameter(description = "The name that needs to be deleted", required = true) @PathParam("username") String username) {
        if (userData.removeUser(username)) {
            return Response.ok().entity("").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{username}")
    @Operation(summary = "Get user by user name",
            responses = {
                    @ApiResponse(description = "The user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid username supplied"),
                    @ApiResponse(responseCode = "400", description = "User not found")})
    public Response getUserByName(
            @Parameter(description = "The name that needs to be fetched. Use user1 for testing. ", required = true) @PathParam("username") String username)
            throws ApiException {
        User user = userData.findUserByName(username);
        if (null != user) {
            return Response.ok().entity(user).build();
        } else {
            throw new NotFoundException(404, "User not found");
        }
    }

    @GET
    @Path("/login")
    @Operation(summary = "Logs user into the system",
            responses = {
                    @ApiResponse(description = "Successfully logged in",
                            content = @Content(schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid username/password supplied")})
    public Response loginUser(
            @Parameter(description = "The user name for login", required = true) @QueryParam("username") String username,
            @Parameter(description = "The password for login in clear text", required = true) @QueryParam("password") String password) {
        return Response.ok()
                .entity("logged in user session:" + System.currentTimeMillis())
                .build();
    }

    @GET
    @Path("/logout")
    @Operation(summary = "Logs out current logged in user session")
    public Response logoutUser() {
        return Response.ok().entity("").build();
    }
}
