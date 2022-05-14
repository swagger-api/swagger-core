package io.swagger.v3.jaxrs2;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.OpenApiContextLocator;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.swagger.v3.jaxrs2.integration.ServletConfigContextUtils.getContextIdFromServletConfig;

public class BootstrapServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        OpenAPI oas = new OpenAPI().openapi("3.0.1");
        Info info = new Info()
                .title("Swagger Sample App - independent config exposed by dedicated servlet")
                .description("This is a sample server Petstore server.  You can find out more about Swagger " +
                        "at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).  For this sample, " +
                        "you can use the api key `special-key` to test the authorization filters.")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact()
                        .email("apiteam@swagger.io"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://www.apache.org/licenses/LICENSE-2.0.html"));

        oas.info(info);
        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true)
                .openAPI31(true)
                .resourcePackages(Stream.of("io.swagger.v3.jaxrs2.it.resources").collect(Collectors.toSet()));

        try {
            new JaxrsOpenApiContextBuilder()
                    .servletConfig(config)
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ctxId = getContextIdFromServletConfig(getServletConfig());
        OpenApiContext ctx = OpenApiContextLocator.getInstance().getOpenApiContext(ctxId);
        OpenAPI oas = ctx.read();

        resp.setStatus(200);

        resp.setContentType("application/yaml");
        try (PrintWriter pw = resp.getWriter()) {
            pw.write(ctx.getOutputYamlMapper().writer(new DefaultPrettyPrinter()).writeValueAsString(oas));
        }
    }
}
