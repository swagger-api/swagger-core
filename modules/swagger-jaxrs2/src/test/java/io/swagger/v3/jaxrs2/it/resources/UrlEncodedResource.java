package io.swagger.v3.jaxrs2.it.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
