package io.swagger.v3.jaxrs2.it.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces("application/json")
public class UrlEncodedResource {
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addUser(@FormParam("id") final String id, @FormParam("name") final String name,
                            @FormParam("gender") final String gender) {
        return Response.status(200).entity("Adding user  " + id + " to the system.").build();
    }
}
