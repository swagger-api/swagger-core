package io.swagger.v3.jakartarest.resources;

import io.swagger.v3.jakartarest.resources.data.UserData;
import io.swagger.v3.jakartarest.resources.model.User;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/user")
@Produces({"application/json", "application/xml"})
@Hidden
public class HiddenAnnotatedUserResource {
    static UserData userData = new UserData();

    @POST
    @Operation(hidden = true, summary = "Create user",
            description = "This can only be done by the logged in user.")
    public Response createUser(
            @Parameter(description = "Created user object", required = true) User user) {
        userData.addUser(user);
        return Response.ok().entity("").build();
    }

    @Path("/user")
    @Produces({"application/json", "application/xml"})
    public class HiddenAnnotatedUserResourceMethodAndData {
        UserData userData = new UserData();

        @POST
        @Hidden
        @Operation(summary = "Create user",
                description = "This can only be done by the logged in user.")
        @Path("/1")
        public Response createUser(
                @Parameter(description = "Created user object", required = true) User user) {
            userData.addUser(user);
            return Response.ok().entity("").build();
        }

        @POST
        @Operation(summary = "Create user",
                description = "This can only be done by the logged in user.")
        @Path("/2")
        public Response createUserWithHiddenBeanProperty(
                @Parameter(description = "Created user object", required = true) UserResourceBean user) {
            return Response.ok().entity("").build();
        }
    }

    public class UserResourceBean {

        @Hidden
        public HiddenAnnotatedUserResourceBean id;
        public String foo;
    }


    public class HiddenAnnotatedUserResourceBean {

        public String id;
        public String foo;
    }
}
