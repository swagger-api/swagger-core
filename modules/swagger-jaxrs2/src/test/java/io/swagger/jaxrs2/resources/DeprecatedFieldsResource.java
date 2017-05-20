package io.swagger.jaxrs2.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public class DeprecatedFieldsResource {
    @GET
    @Path("/")
    @io.swagger.oas.annotations.Operation(deprecated = true)
    public Response deprecatedMethod() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/")
    @io.swagger.oas.annotations.Operation()
    public Response nonDeprecatedMethod() {
        return Response.ok().entity("ok").build();
    }
}