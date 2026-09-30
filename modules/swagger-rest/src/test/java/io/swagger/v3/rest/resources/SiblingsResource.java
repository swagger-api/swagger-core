package io.swagger.v3.rest.resources;

import io.swagger.v3.rest.resources.siblings.Pet;
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
