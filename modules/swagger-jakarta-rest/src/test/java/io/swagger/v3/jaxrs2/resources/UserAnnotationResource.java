package io.swagger.v3.jaxrs2.resources;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("test")
@Produces(MediaType.APPLICATION_JSON)
public class UserAnnotationResource {
    @Path("/status")
    @GET
    @UserAnnotation(name = "test")
    @NotNull
    public String getStatus() {
        return "{\"status\":\"OK!\"}";
    }
}
