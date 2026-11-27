package io.swagger.v3.rest.integration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Webhooks;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import org.testng.annotations.Test;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Path;
import java.util.Collections;
import java.util.Set;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class RestAnnotationScannerTest {
    @Path("/scanner")
    protected static class Resource {
        static {
            if (true) {
                throw new AssertionError("Scanning must not initialize resources");
            }
        }
    }

    @OpenAPIDefinition
    protected static class Definition {
    }

    @Webhooks
    protected static class Hooks {
    }

    @ApplicationPath("/application")
    protected static class App {
    }

    @Test
    public void shouldDiscoverAllSupportedAnnotationsAndRespectApplicationPathOption() {
        SwaggerConfiguration configuration = new SwaggerConfiguration()
                .resourcePackages(Collections.singleton(getClass().getPackage().getName()));
        RestAnnotationScanner scanner = new RestAnnotationScanner().openApiConfiguration(configuration);

        Set<Class<?>> classes = scanner.classes();
        assertTrue(classes.contains(Resource.class));
        assertTrue(classes.contains(Definition.class));
        assertTrue(classes.contains(Hooks.class));
        assertFalse(classes.contains(App.class));

        configuration.setAlwaysResolveAppPath(true);
        assertTrue(scanner.classes().contains(App.class));
    }

    @Test
    public void shouldRespectResourcePackageFilter() {
        RestAnnotationScanner scanner = new RestAnnotationScanner().openApiConfiguration(
                new SwaggerConfiguration().resourcePackages(Collections.singleton("com.my.project.resources")));

        assertFalse(scanner.classes().contains(Resource.class));
        assertTrue(scanner.classes().contains(com.my.project.resources.ResourceInPackageA.class));
    }
}
