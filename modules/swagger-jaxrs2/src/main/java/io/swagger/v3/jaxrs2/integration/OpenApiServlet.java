package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.core.filter.OpenAPISpecFilter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.OpenApiContextLocator;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.getContextIdFromServletConfig;

public class OpenApiServlet extends HttpServlet {

    private static Logger LOGGER = LoggerFactory.getLogger(OpenApiServlet.class);

    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_YAML = "application/yaml";
    public static final String ACCEPT_HEADER = "Accept";

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        String ctxId = getContextIdFromServletConfig(config);
        try {
            new ServletOpenApiContextBuilder()
                    .servletConfig(config)
                    .ctxId(ctxId)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            e.printStackTrace();
        }
    }

    // TODO move to own servlet non jaxrs project and reference from there
    // TODO cleanup and errors
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String ctxId = getContextIdFromServletConfig(getServletConfig());
        OpenApiContext ctx = OpenApiContextLocator.getInstance().getOpenApiContext(ctxId);
        OpenAPI oas = ctx.read();

        if (oas != null) {
            if (ctx.getOpenApiConfiguration() != null && ctx.getOpenApiConfiguration().getFilterClass() != null) {
                try {
                    OpenAPISpecFilter filterImpl = (OpenAPISpecFilter) Class.forName(ctx.getOpenApiConfiguration().getFilterClass()).newInstance();
                    SpecFilter f = new SpecFilter();
                    oas = f.filter(oas, filterImpl, getQueryParams(req.getQueryString()), getCookies(req.getCookies()),
                            getHeaders(req));
                } catch (Exception e) {
                    LOGGER.error("failed to load filter", e);
                }
            }
        }

        String type = "json";

        String acceptHeader = req.getHeader(ACCEPT_HEADER);
        if (!StringUtils.isBlank(acceptHeader) && acceptHeader.toLowerCase().contains(APPLICATION_YAML)) {
            type = "yaml";
        } else {
            // check URL:
            if (req.getRequestURL().toString().toLowerCase().endsWith("yaml")) {
                type = "yaml";
            }
        }

        boolean pretty = false;
        if (ctx.getOpenApiConfiguration() != null && Boolean.TRUE.equals(ctx.getOpenApiConfiguration().isPrettyPrint())) {
            pretty = true;
        }

        resp.setStatus(200);

        if (type.equalsIgnoreCase("yaml")) {
            resp.setContentType(APPLICATION_YAML);
            PrintWriter pw = resp.getWriter();
            pw.write(pretty ? Yaml.pretty(oas) : Yaml.mapper().writeValueAsString(oas));
            pw.close();
        } else {
            resp.setContentType(APPLICATION_JSON);
            PrintWriter pw = resp.getWriter();
            pw.write(pretty ? Json.pretty(oas) : Json.mapper().writeValueAsString(oas));
            pw.close();
        }

    }

    public static MultivaluedHashMap<String, String> getQueryParams(String queryString) {
        MultivaluedHashMap<String, String> queryParameters = new MultivaluedHashMap<>();

        if (StringUtils.isEmpty(queryString)) {
            return queryParameters;
        }

        String[] parameters = queryString.split("&");

        for (String parameter : parameters) {
            String[] keyValuePair = parameter.split("=");
            queryParameters.put(keyValuePair[0], Collections.singletonList(keyValuePair[1]));
        }
        return queryParameters;
    }

    private static Map<String, String> getCookies(Cookie[] cookies) {
        Map<String, String> mapOfCookies = new HashMap<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                mapOfCookies.put(cookie.getName(), cookie.getValue());
            }
        }
        return mapOfCookies;
    }

    public static Map<String, List<String>> getHeaders(HttpServletRequest req) {
        if (req.getHeaderNames() == null) {
            return Collections.emptyMap();
        } else {
            return Collections
                    .list(req.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            header -> Collections.list(req.getHeaders(header))));
        }
    }
}
