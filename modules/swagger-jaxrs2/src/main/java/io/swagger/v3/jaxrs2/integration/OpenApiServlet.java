package io.swagger.v3.jaxrs2.integration;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import io.swagger.v3.core.filter.OpenAPISpecFilter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.jaxrs2.util.ServletUtils;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.OpenApiContextLocator;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.getContextIdFromServletConfig;

public class OpenApiServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiServlet.class);

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
            LOGGER.error("Failed to initialize OpenAPI servlet context", e);
        }
    }

    // TODO move to own servlet non jaxrs project and reference from there
    // TODO cleanup and errors
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String ctxId = getContextIdFromServletConfig(getServletConfig());
        OpenApiContext ctx = OpenApiContextLocator.getInstance().getOpenApiContext(ctxId);
        OpenAPI oas = ctx.read();

        if (oas != null && ctx.getOpenApiConfiguration() != null) {
            if (ctx.getOpenApiConfiguration().getFilterClass() != null) {
                try {
                    OpenAPISpecFilter filterImpl = (OpenAPISpecFilter) Class.forName(ctx.getOpenApiConfiguration().getFilterClass()).newInstance();
                    SpecFilter f = new SpecFilter();
                    oas = f.filter(oas, filterImpl, ServletUtils.getQueryParams(req.getParameterMap()),
                            ServletUtils.getCookies(req.getCookies()), ServletUtils.getHeaders(req));
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

        boolean pretty = ctx.getOpenApiConfiguration() != null && Boolean.TRUE.equals(ctx.getOpenApiConfiguration().isPrettyPrint());

        resp.setStatus(200);

        if (type.equalsIgnoreCase("yaml")) {
            resp.setContentType(APPLICATION_YAML);
            try (PrintWriter pw = resp.getWriter()) {
                pw.write(pretty ? ctx.getOutputYamlMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(oas) : ctx.getOutputYamlMapper().writeValueAsString(oas));
            }
        } else {
            resp.setContentType(APPLICATION_JSON);
            try (PrintWriter pw = resp.getWriter()) {
                pw.write(pretty ? ctx.getOutputJsonMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(oas) : ctx.getOutputJsonMapper().writeValueAsString(oas));
            }
        }

    }
}
