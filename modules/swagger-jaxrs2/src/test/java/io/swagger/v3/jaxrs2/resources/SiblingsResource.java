package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.jaxrs2.resources.siblings.Pet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/test")
public class SiblingsResource {
    @GET
    public Pet getCart() {
        return null;
    }
}
