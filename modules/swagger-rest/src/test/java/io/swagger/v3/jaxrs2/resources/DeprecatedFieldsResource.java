package io.swagger.v3.jaxrs2.resources;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

public class DeprecatedFieldsResource {
    @GET
    @Path("/")
    @Operation(deprecated = true)
    public Response deprecatedMethod() {
        return Response.ok().entity("ok").build();
    }
}