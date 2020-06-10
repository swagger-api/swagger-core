package io.swagger.v3.jaxrs2.resources;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
