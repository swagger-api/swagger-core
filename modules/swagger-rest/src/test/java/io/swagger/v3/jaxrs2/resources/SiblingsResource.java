package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.siblings.Pet;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/test")
public class SiblingsResource {
    @GET
    @Schema(description = "Cart Pet")
    public Pet getCart() {
        return null;
    }
}
