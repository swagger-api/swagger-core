package io.swagger.v3.jaxrs2.integration.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.core.filter.OpenAPISpecFilter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;

public abstract class BaseOpenApiResource {

    private static Logger LOGGER = LoggerFactory.getLogger(BaseOpenApiResource.class);

    protected Response getOpenApi(final HttpHeaders headers,
                                  final Application app,
                                  final UriInfo uriInfo,
                                  final String type) throws Exception {

        final OpenApiContext ctx = new JaxrsOpenApiContextBuilder()
                .application(app)
                .resourcePackages(resourcePackages)
                .configLocation(configLocation)
                .openApiConfiguration(openApiConfiguration)
                .buildContext(true);
        OpenAPI oas = ctx.read();
        boolean pretty = false;
        if (ctx.getOpenApiConfiguration() != null && Boolean.TRUE.equals(ctx.getOpenApiConfiguration().isPrettyPrint())) {
            pretty = true;
        }

        if (oas != null) {
            if (ctx.getOpenApiConfiguration() != null && ctx.getOpenApiConfiguration().getFilterClass() != null) {
                try {
                    final OpenAPISpecFilter filterImpl = (OpenAPISpecFilter) Class.forName(ctx.getOpenApiConfiguration().getFilterClass()).newInstance();
                    final SpecFilter f = new SpecFilter();
                    oas = f.filter(oas, filterImpl, getQueryParams(uriInfo.getQueryParameters()), getCookies(headers),
                            getHeaders(headers));
                } catch (final Exception e) {
                    LOGGER.error("failed to load filter", e);
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

    private static Map<String, List<String>> getQueryParams(final MultivaluedMap<String, String> params) {
        final Map<String, List<String>> output = new HashMap<String, List<String>>();
        if (params != null) {
            for (final String key : params.keySet()) {
                final List<String> values = params.get(key);
                output.put(key, values);
            }
        }
        return output;
    }

    private static Map<String, String> getCookies(final HttpHeaders headers) {
        final Map<String, String> output = new HashMap<String, String>();
        if (headers != null) {
            for (final String key : headers.getCookies().keySet()) {
                final Cookie cookie = headers.getCookies().get(key);
                output.put(key, cookie.getValue());
            }
        }
        return output;
    }

    private static Map<String, List<String>> getHeaders(final HttpHeaders headers) {
        final Map<String, List<String>> output = new HashMap<String, List<String>>();
        if (headers != null) {
            for (final String key : headers.getRequestHeaders().keySet()) {
                final List<String> values = headers.getRequestHeaders().get(key);
                output.put(key, values);
            }
        }
        return output;
    }

    protected String configLocation;

    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(final String configLocation) {
        this.configLocation = configLocation;
    }

    public BaseOpenApiResource configLocation(final String configLocation) {
        setConfigLocation(configLocation);
        return this;
    }

    protected Set<String> resourcePackages;

    public Set<String> getResourcePackages() {
        return resourcePackages;
    }

    public void setResourcePackages(final Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
    }

    public BaseOpenApiResource resourcePackages(final Set<String> resourcePackages) {
        setResourcePackages(resourcePackages);
        return this;
    }

    protected OpenAPIConfiguration openApiConfiguration;

    public OpenAPIConfiguration getOpenApiConfiguration() {
        return openApiConfiguration;
    }

    public void setOpenApiConfiguration(final OpenAPIConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
    }

    public BaseOpenApiResource openApiConfiguration(final OpenAPIConfiguration openApiConfiguration) {
        setOpenApiConfiguration(openApiConfiguration);
        return this;
    }

}
