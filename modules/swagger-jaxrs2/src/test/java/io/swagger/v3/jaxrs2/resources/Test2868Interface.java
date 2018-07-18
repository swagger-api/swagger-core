package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public interface Test2868Interface {
    @GET
    @Path("{id}/foo")
    String getFoo();
}