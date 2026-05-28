package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("{globalPathParam}")
public class Ticket4878Resource {

    public Ticket4878Resource(@PathParam("globalPathParam") @Schema($comment="3.1 property for global path param") String globalPathParam) {}

    @GET
    @Path("{localPathParam}")
    public void getMethod(@PathParam("localPathParam") @Schema($comment="3.1 property for local path param") String localPathParam) {}
}
