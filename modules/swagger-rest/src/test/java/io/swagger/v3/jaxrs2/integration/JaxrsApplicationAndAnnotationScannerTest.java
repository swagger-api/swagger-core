package io.swagger.v3.jaxrs2.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.my.project.resources.ResourceInPackageB;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.my.project.resources.ResourceInPackageA;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.integration.GenericOpenApiContext;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class JaxrsApplicationAndAnnotationScannerTest {
    private JaxrsApplicationAndAnnotationScanner scanner;

    @Path("/app")
    protected static class ResourceInApplication {
        @Operation(operationId = "test.")
        @GET
        public void getTest(@Parameter(name = "test") ArrayList<String> tenantId) {
            return;
        }
    }

    @BeforeMethod
    public void setUp() {
        scanner = new JaxrsApplicationAndAnnotationScanner();
        
        scanner.setApplication(new Application() {
            @Override
            public Set<Class<?>> getClasses() {
                return Collections.singleton(ResourceInApplication.class);
            }
        });
    }
    
    @Test(description = "scan classes from all packages and Application")
    public void shouldScanClassesFromAllPackagesAndApplication() throws Exception {
        assertTrue(scanner.classes().contains(ResourceInPackageA.class));
        assertTrue(scanner.classes().contains(ResourceInPackageB.class));
        assertTrue(scanner.classes().contains(ResourceInApplication.class));
    }
    
    @Test(description = "scan classes from the packages and Application")
    public void shouldScanClassesFromPackagesAndApplication() throws Exception {
        SwaggerConfiguration openApiConfiguration = new SwaggerConfiguration();
        openApiConfiguration.setResourcePackages(Collections.singleton("com.my.project.resources"));
        scanner.setConfiguration(openApiConfiguration);
        
        assertEquals(scanner.classes().size(), 2);
        assertTrue(scanner.classes().contains(ResourceInPackageA.class));
        assertTrue(scanner.classes().contains(ResourceInApplication.class));
    }
    
    @Test(description = "scan a simple resource from packages")
    public void shouldScanClassesFromPackages() throws Exception {
        SwaggerConfiguration config = new SwaggerConfiguration()
                .resourcePackages(Stream.of("com.my.project.resources", "org.my.project.resources").collect(Collectors.toSet()))
                .openAPI(new OpenAPI().info(new Info().description("TEST INFO DESC")));

        OpenApiContext ctx = new GenericOpenApiContext<>()
                .openApiConfiguration(config)
                .openApiReader(new Reader(config))
                .openApiScanner(scanner.application(null).openApiConfiguration(config))
                .init();

        OpenAPI openApi = ctx.read();

        assertNotNull(openApi);
        assertEquals(openApi.getPaths().keySet(), Arrays.asList("/packageA", "/packageB"));

    }
    
    @Test(description = "scan a simple resource from packages using configuration file")
    public void shouldScanClassesFromPackagesUsingConfigurationFile() throws Exception {
        String configPath = "/integration/openapi-configuration.json";
        OpenApiContext ctx = new XmlWebOpenApiContext<>().configLocation(configPath).init();
        OpenAPI openApi = ctx.read();

        assertNotNull(openApi);
        assertEquals(openApi.getPaths().keySet(), Arrays.asList("/packageA", "/packageB"));
    }
}
