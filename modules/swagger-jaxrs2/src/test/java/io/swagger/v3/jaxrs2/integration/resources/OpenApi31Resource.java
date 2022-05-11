package io.swagger.v3.jaxrs2.integration.resources;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.integration.SwaggerConfiguration;

import javax.servlet.ServletConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/openapi31")
public class OpenApi31Resource extends BaseOpenApiResource {

    @Context
    ServletConfig config;

    @Context
    Application app;

    @GET
    @Produces({"application/yaml"})
    @Operation(hidden = true)
    public Response getOpenApiYaml(@Context HttpHeaders headers, @Context UriInfo uriInfo) throws Exception {
        super.setOpenApiConfiguration(new SwaggerConfiguration().openAPI31(true));
        return super.getOpenApi(headers, config, app, uriInfo, "yaml");
    }
}
