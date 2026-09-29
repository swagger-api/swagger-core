package io.swagger.v3.rest.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

public class DefaultResponseResource {

    @GET
    @Path("/")
    public String test() {
        return null;
    }

}
