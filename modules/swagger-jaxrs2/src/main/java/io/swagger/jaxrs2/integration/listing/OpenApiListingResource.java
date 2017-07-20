package io.swagger.jaxrs2.integration.listing;

import io.swagger.oas.integration.OpenApiContext;
import io.swagger.oas.models.OpenAPI;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static io.swagger.jaxrs2.integration.ContextUtils.*;

@Path("/openApi.{type:json|yaml}")
public class OpenApiListingResource {
    @Context
    ServletConfig config;

    @Context
    Application app;

    @GET
    @Produces({MediaType.APPLICATION_JSON, "application/yaml"})
    public Response getOpenApi(@PathParam("type") String type) throws Exception {

        String ctxId = OpenApiContext.OPENAPI_CONTEXT_ID_DEFAULT;
        if (config != null) {
            ctxId = getInitParam(config, OpenApiContext.OPENAPI_CONTEXT_ID_KEY);
            if (StringUtils.isBlank(ctxId)) {
                ctxId = OpenApiContext.OPENAPI_CONTEXT_ID_PREFIX + "servlet." + config.getServletName();
            }
        }
        OpenApiContext ctx = getOrBuildContext(ctxId, app, config, configLocation, resourcePackage, null);
        OpenAPI oas = ctx.read();

        String aa = Json.mapper().writeValueAsString(oas);
        System.out.println(aa);
        //OpenAPI oas = (OpenAPI)config.getServletContext().getAttribute("oas");
        if (StringUtils.isNotBlank(type) && type.trim().equalsIgnoreCase("yaml")) {
            return Response.status(Response.Status.OK)
                    .entity(Yaml.mapper().writeValueAsString(oas))
                    .type("application/yaml")
                    .build();
        } else {
            return Response.status(Response.Status.OK)
                    .entity(Json.mapper().writeValueAsString(oas))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }

    private String configLocation;

    public String getConfigLocation() {
        return configLocation;
    }
    public void setConfigLocation (String configLocation) {
        this.configLocation = configLocation;
    }

    public OpenApiListingResource configLocation(String configLocation) {
        setConfigLocation(configLocation);
        return this;
    }

    private String resourcePackage;

    public String getResourcePackage() {
        return resourcePackage;
    }
    public void setResourcePackage (String resourcePackage) {
        this.resourcePackage = resourcePackage;
    }

    public OpenApiListingResource resourcePackage(String resourcePackage) {
        setResourcePackage(resourcePackage);
        return this;
    }

}
