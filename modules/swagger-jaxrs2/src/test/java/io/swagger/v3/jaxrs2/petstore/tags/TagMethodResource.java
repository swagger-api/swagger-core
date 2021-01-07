package io.swagger.v3.jaxrs2.petstore.tags;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

public class TagMethodResource {
    @GET
    @Path("/tagsinmethod")
    @Tag(name = "Third Tag")
    @Tag(name = "Second Tag")
    @Tag(name = "Fourth Tag Full", description = "desc", externalDocs = @ExternalDocumentation(description = "docs desc"))
    public Response getTags() {
        return Response.ok().entity("ok").build();
    }
}
