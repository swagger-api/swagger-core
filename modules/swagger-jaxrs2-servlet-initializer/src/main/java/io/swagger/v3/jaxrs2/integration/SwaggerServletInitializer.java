package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Webhooks;
import io.swagger.v3.oas.integration.IgnoredPackages;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * deprecated since 2.1.2. Please use io.swagger.v3.jaxrs2.integration.SwaggerServletInitializer in
 * swagger-jaxrs2-servlet-initializer-v2
 */
@Deprecated
@HandlesTypes({Path.class, OpenAPIDefinition.class, ApplicationPath.class, Webhooks.class})
public class SwaggerServletInitializer implements ServletContainerInitializer {

    static final Set<String> ignored = new HashSet();

    static {
        ignored.addAll(IgnoredPackages.ignored);
    }

    public SwaggerServletInitializer() {
    }

    @Override
    public void onStartup(Set<Class<?>> classes, ServletContext servletContext) throws ServletException {
        if (classes != null && ! classes.isEmpty()) {
            Set<Class<?>> resources = new LinkedHashSet();
            classes.stream()
                    .filter(c -> ignored.stream().noneMatch(i -> c.getName().startsWith(i)))
                    .forEach(resources::add);
            if (!resources.isEmpty()) {
                // init context
                try {
                    SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                            .resourceClasses(resources.stream().map(Class::getName).collect(Collectors.toSet()));

                    new JaxrsOpenApiContextBuilder()
                            .openApiConfiguration(oasConfig)
                            .buildContext(true);
                } catch (OpenApiConfigurationException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
    }

}
