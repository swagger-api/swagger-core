package io.swagger.v3.jaxrs2.resources.generics.ticket3426;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/inheritExample")
public class Ticket3426Resource extends Parent<String> {
    @Override
    @GET
    @Path("/{input}")
    public String get(@PathParam("input") String input) {
        return super.get(input);
    }
}
