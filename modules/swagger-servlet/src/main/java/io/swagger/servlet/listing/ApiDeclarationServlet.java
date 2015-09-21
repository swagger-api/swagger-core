package io.swagger.servlet.listing;

import io.swagger.config.Scanner;
import io.swagger.config.SwaggerConfig;
import io.swagger.models.Swagger;
import io.swagger.servlet.Reader;
import io.swagger.util.Json;
import io.swagger.util.Yaml;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The <code>ApiDeclarationServlet</code> class implements servlet which returns the Swagger definition.
 */
public class ApiDeclarationServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws javax.servlet.ServletException {
        super.init(servletConfig);

        final ServletContext servletContext = servletConfig.getServletContext();
        final Scanner scanner = (Scanner) servletContext.getAttribute("scanner");
        if (scanner != null) {
            Swagger swagger = (Swagger) servletContext.getAttribute("swagger");
            if (swagger == null) {
                swagger = new Swagger();
            }
            final SwaggerConfig configurator = (SwaggerConfig) servletContext.getAttribute("reader");
            if (configurator != null) {
                configurator.configure(swagger);
            }
            final Set<Class<?>> classes = scanner.classes();
            if (classes != null) {
                Reader.read(swagger, classes);
            }
            servletContext.setAttribute("swagger", swagger);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Swagger swagger = (Swagger) getServletContext().getAttribute("swagger");
        if (swagger == null) {
            response.setStatus(404);
            return;
        }
        final String pathInfo = request.getPathInfo();
        if ("/swagger.json".equals(pathInfo)) {
            response.getWriter().println(Json.mapper().writeValueAsString(swagger));
        } else if ("/swagger.yaml".equals(pathInfo)) {
            response.getWriter().println(Yaml.mapper().writeValueAsString(swagger));
        } else {
            response.setStatus(404);
        }
    }
}
