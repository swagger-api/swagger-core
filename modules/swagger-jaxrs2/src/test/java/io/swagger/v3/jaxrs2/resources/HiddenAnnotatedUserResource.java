package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.data.UserData;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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
