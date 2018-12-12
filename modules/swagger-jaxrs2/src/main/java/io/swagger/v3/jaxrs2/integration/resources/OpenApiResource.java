package io.swagger.v3.jaxrs2.integration.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import io.swagger.v3.oas.annotations.Operation;

@Path("/openapi.{type:json|yaml}")
public class OpenApiResource extends BaseOpenApiResource {

    @Context
    Application app;

    @GET
    @Produces({MediaType.APPLICATION_JSON, "application/yaml"})
    @Operation(hidden = true)
    public Response getOpenApi(@Context final HttpHeaders headers,
                               @Context final UriInfo uriInfo,
                               @PathParam("type") final String type) throws Exception {

        return super.getOpenApi(headers, app, uriInfo, type);
    }
}
