package io.swagger.v3.rest.resources;

import jakarta.ws.rs.Path;

@Path("/v1")
public class ClassPathParentResource {
    @Path("parent")
    public ClassPathSubResource getSubResource() {
        return new ClassPathSubResource();
    }
}