package io.swagger.v3.jaxrs2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

public class DefaultResponseResource {

    @GET
    @Path("/")
    public String test() {
        return null;
    }

}
