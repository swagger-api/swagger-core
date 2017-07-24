package io.swagger.jaxrs2.integration.listing;

import io.swagger.core.filter.SpecFilter;
import io.swagger.core.filter.SwaggerSpecFilter;
import io.swagger.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.oas.integration.OpenApiConfiguration;
import io.swagger.oas.integration.OpenApiContext;
import io.swagger.oas.models.OpenAPI;
import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.swagger.jaxrs2.integration.ContextUtils.getContextIdFromServletConfig;

public abstract class BaseOpenApiListingResource {

    protected Response getOpenApi(HttpHeaders headers,
                                  ServletConfig config,
                                  Application app,
                                  UriInfo uriInfo,
                                  String type) throws Exception {

        String ctxId = getContextIdFromServletConfig(config);
        OpenApiContext ctx = new JaxrsOpenApiContextBuilder()
                .servletConfig(config)
                .application(app)
                .resourcePackage(resourcePackage)
                .configLocation(configLocation)
                .openApiConfiguration(openApiConfiguration)
                .ctxId(ctxId)
                .buildContext(true);
        OpenAPI oas = ctx.read();
        boolean pretty = false;
        if (ctx.getOpenApiConfiguration() != null && ctx.getOpenApiConfiguration().isPrettyPrint()) {
            pretty = true;
        }


        if (oas != null) {
            if (ctx.getOpenApiConfiguration() != null && ctx.getOpenApiConfiguration().getFilterClass() != null) {
                try {
                    SwaggerSpecFilter filterImpl = (SwaggerSpecFilter) Class.forName(ctx.getOpenApiConfiguration().getFilterClass()).newInstance();
                    SpecFilter f = new SpecFilter();
                    oas = f.filter(oas, filterImpl, getQueryParams(uriInfo.getQueryParameters()), getCookies(headers),
                            getHeaders(headers));
                } catch (Exception e) {
                    // TODO
                    // LOGGER.error("failed to load filter", e);
                }
            }
        }

        if (oas == null) {
            return Response.status(404).build();
        }

        if (StringUtils.isNotBlank(type) && type.trim().equalsIgnoreCase("yaml")) {
            return Response.status(Response.Status.OK)
                    .entity(pretty ? Yaml.pretty(oas) : Yaml.mapper().writeValueAsString(oas))
                    .type("application/yaml")
                    .build();
        } else {
            return Response.status(Response.Status.OK)
                    .entity(pretty ? Json.pretty(oas) : Json.mapper().writeValueAsString(oas))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }

    private static Map<String, List<String>> getQueryParams(MultivaluedMap<String, String> params) {
        Map<String, List<String>> output = new HashMap<String, List<String>>();
        if (params != null) {
            for (String key : params.keySet()) {
                List<String> values = params.get(key);
                output.put(key, values);
            }
        }
        return output;
    }

    private static Map<String, String> getCookies(HttpHeaders headers) {
        Map<String, String> output = new HashMap<String, String>();
        if (headers != null) {
            for (String key : headers.getCookies().keySet()) {
                Cookie cookie = headers.getCookies().get(key);
                output.put(key, cookie.getValue());
            }
        }
        return output;
    }

    private static Map<String, List<String>> getHeaders(HttpHeaders headers) {
        Map<String, List<String>> output = new HashMap<String, List<String>>();
        if (headers != null) {
            for (String key : headers.getRequestHeaders().keySet()) {
                List<String> values = headers.getRequestHeaders().get(key);
                output.put(key, values);
            }
        }
        return output;
    }

    protected String configLocation;

    public String getConfigLocation() {
        return configLocation;
    }
    public void setConfigLocation (String configLocation) {
        this.configLocation = configLocation;
    }

    public BaseOpenApiListingResource configLocation(String configLocation) {
        setConfigLocation(configLocation);
        return this;
    }

    protected String resourcePackage;

    public String getResourcePackage() {
        return resourcePackage;
    }
    public void setResourcePackage (String resourcePackage) {
        this.resourcePackage = resourcePackage;
    }

    public BaseOpenApiListingResource resourcePackage(String resourcePackage) {
        setResourcePackage(resourcePackage);
        return this;
    }

    protected OpenApiConfiguration openApiConfiguration;

    public OpenApiConfiguration getOpenApiConfiguration() {
        return openApiConfiguration;
    }
    public void setOpenApiConfiguration (OpenApiConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
    }

    public BaseOpenApiListingResource openApiConfiguration(OpenApiConfiguration openApiConfiguration) {
        setOpenApiConfiguration(openApiConfiguration);
        return this;
    }

}
