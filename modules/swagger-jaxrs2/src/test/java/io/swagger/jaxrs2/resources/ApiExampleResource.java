package io.swagger.jaxrs2.resources;


import io.swagger.oas.annotations.Operation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Created by RafaelLopez on 5/16/17.
 */
public class ApiExampleResource {
    @GET
    @Path("/")
    @Operation(summary = "Operation Summary", description = "Operation Description")
    public Response getSummaryAndDescription() {
        return Response.ok().entity("ok").build();
    }

    @GET
    @Path("/")
    @Operation(deprecated = true)
    public Response deprecatedMethod() {
        return Response.ok().entity("ok").build();
    }
}
