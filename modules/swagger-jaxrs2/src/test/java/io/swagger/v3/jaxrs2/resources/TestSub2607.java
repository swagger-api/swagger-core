package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
public class TestSub2607 {
    @GET
    @Path("version")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSubResourceVersion() {
        return "1.0.0";
    }
}