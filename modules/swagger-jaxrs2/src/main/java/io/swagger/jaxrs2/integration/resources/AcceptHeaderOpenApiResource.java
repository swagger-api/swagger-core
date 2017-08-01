package io.swagger.jaxrs2.integration.resources;

import javax.servlet.ServletConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/openApi")
public class AcceptHeaderOpenApiResource extends BaseOpenApiResource {
    @Context
    ServletConfig config;

    @Context
    Application app;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getOpenApiJson( @Context HttpHeaders headers,
                                @Context UriInfo uriInfo) throws Exception {

        return super.getOpenApi(headers, config, app, uriInfo, "json");
    }

    @GET
    @Produces({"application/yaml"})
    public Response getOpenApiYaml( @Context HttpHeaders headers,
                                @Context UriInfo uriInfo) throws Exception {

        return super.getOpenApi(headers, config, app, uriInfo, "yaml");
    }
}
