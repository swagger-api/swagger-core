package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Parameter;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/test")
public class Ticket4412Resource {
    @Path("/sws/{var:.*}")
    @GET
    @Produces(MediaType.TEXT_XML)
    public List<String> getCart(@PathParam("var") String var) {
        return null;
    }
}
