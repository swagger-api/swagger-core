package io.swagger.v3.jaxrs2.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public class TestSub2607 {
    @GET
    @Path("version")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSubResourceVersion() {
        return "1.0.0";
    }
}