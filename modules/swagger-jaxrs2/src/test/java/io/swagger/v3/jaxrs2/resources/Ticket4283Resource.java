package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/item")
public class Ticket4283Resource {

    @Context
    UriInfo uriInfo;

    @Path("/content")
    public Ticket4283SubResource getItemContentResource() {
        return new Ticket4283SubResource();
    }

    @GET
    @Produces("application/xml")
    public Item get() { return null; }

    public static class Item {
        public String foo;
    }
}
