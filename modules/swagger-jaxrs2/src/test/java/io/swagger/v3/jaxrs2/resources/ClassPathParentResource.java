package io.swagger.v3.jaxrs2.resources;

import jakarta.ws.rs.Path;

@Path("/v1")
public class ClassPathParentResource {
    @Path("parent")
    public ClassPathSubResource getSubResource() {
        return new ClassPathSubResource();
    }
}