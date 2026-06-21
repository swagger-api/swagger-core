package io.swagger.v3.jakartarest.resources;

import io.swagger.v3.oas.annotations.Parameter;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
