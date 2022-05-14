package io.swagger.v3.jaxrs2.integration.resources;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import io.swagger.v3.core.filter.OpenAPISpecFilter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.getContextIdFromServletConfig;

public abstract class BaseOpenApiResource {

    private static Logger LOGGER = LoggerFactory.getLogger(BaseOpenApiResource.class);

    protected String getContextId(ServletConfig config) {
        return getContextIdFromServletConfig(config);
    }

    protected Response getOpenApi(HttpHeaders headers,
                                  ServletConfig config,
                                  Application app,
                                  UriInfo uriInfo,
                                  String type) throws Exception {

        String ctxId = getContextId(config);
        OpenApiContext ctx = new JaxrsOpenApiContextBuilder()
                .servletConfig(config)
                .application(app)
                .resourcePackages(resourcePackages)
                .configLocation(configLocation)
                .openApiConfiguration(openApiConfiguration)
                .ctxId(ctxId)
                .buildContext(true);
        OpenAPI oas = ctx.read();
        boolean pretty = ctx.getOpenApiConfiguration() != null && Boolean.TRUE.equals(ctx.getOpenApiConfiguration().isPrettyPrint());

        if (oas != null && ctx.getOpenApiConfiguration() != null) {
            if (ctx.getOpenApiConfiguration().getFilterClass() != null) {
                try {
                    OpenAPISpecFilter filterImpl = (OpenAPISpecFilter) Class.forName(ctx.getOpenApiConfiguration().getFilterClass()).newInstance();
                    SpecFilter f = new SpecFilter();
                    oas = f.filter(oas, filterImpl, getQueryParams(uriInfo.getQueryParameters()), getCookies(headers),
                            getHeaders(headers));
                } catch (Exception e) {
                    LOGGER.error("failed to load filter", e);
                }
            }
        }

        if (oas == null) {
            return Response.status(404).build();
        }

        if (StringUtils.isNotBlank(type) && type.trim().equalsIgnoreCase("yaml")) {
            return Response.status(Response.Status.OK)
                    .entity(pretty ?
                            ctx.getOutputYamlMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(oas) :
                            ctx.getOutputYamlMapper().writeValueAsString(oas))
                    .type("application/yaml")
                    .build();
        } else {
            return Response.status(Response.Status.OK)
                    .entity(pretty ?
                            ctx.getOutputJsonMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(oas) :
                            ctx.getOutputJsonMapper().writeValueAsString(oas))
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .build();
        }
    }

    private static Map<String, List<String>> getQueryParams(MultivaluedMap<String, String> params) {
        Map<String, List<String>> output = new HashMap<>();
        if (params != null) {
            params.forEach(output::put);
        }
        return output;
    }

    private static Map<String, String> getCookies(HttpHeaders headers) {
        Map<String, String> output = new HashMap<>();
        if (headers != null) {
            headers.getCookies().forEach((k, v) -> output.put(k, v.getValue()));
        }
        return output;
    }

    private static Map<String, List<String>> getHeaders(HttpHeaders headers) {
        Map<String, List<String>> output = new HashMap<>();
        if (headers != null) {
            headers.getRequestHeaders().forEach(output::put);
        }
        return output;
    }

    protected String configLocation;

    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public BaseOpenApiResource configLocation(String configLocation) {
        setConfigLocation(configLocation);
        return this;
    }

    protected Set<String> resourcePackages;

    public Set<String> getResourcePackages() {
        return resourcePackages;
    }

    public void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
    }

    public BaseOpenApiResource resourcePackages(Set<String> resourcePackages) {
        setResourcePackages(resourcePackages);
        return this;
    }

    protected OpenAPIConfiguration openApiConfiguration;

    public OpenAPIConfiguration getOpenApiConfiguration() {
        return openApiConfiguration;
    }

    public void setOpenApiConfiguration(OpenAPIConfiguration openApiConfiguration) {
        this.openApiConfiguration = openApiConfiguration;
    }

    public BaseOpenApiResource openApiConfiguration(OpenAPIConfiguration openApiConfiguration) {
        setOpenApiConfiguration(openApiConfiguration);
        return this;
    }

}
