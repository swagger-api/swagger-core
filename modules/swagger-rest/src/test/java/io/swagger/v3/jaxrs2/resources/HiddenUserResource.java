package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.data.UserData;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/user")
@Produces({"application/json", "application/xml"})
public class HiddenUserResource {
    static UserData userData = new UserData();

    @POST
    @Operation(hidden = true, summary = "Create user",
            description = "This can only be done by the logged in user.")
    public Response createUser(
            @Parameter(description = "Created user object", required = true) User user) {
        userData.addUser(user);
        return Response.ok().entity("").build();
    }
}
