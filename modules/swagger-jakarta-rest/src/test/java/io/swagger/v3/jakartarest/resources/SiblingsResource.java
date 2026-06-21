package io.swagger.v3.jakartarest.resources;

import io.swagger.v3.jakartarest.resources.siblings.Pet;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/test")
public class SiblingsResource {
    @GET
    @Schema(description = "Cart Pet")
    public Pet getCart() {
        return null;
    }
}
