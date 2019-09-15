package io.swagger.v3.jaxrs2.integration;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.integration.GenericOpenApiContext;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.my.project.resources.ResourceInPackageA;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.core.Application;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class JaxrsApplicationAndResourcePackagesAnnotationScannerTest {

    private JaxrsApplicationAndResourcePackagesAnnotationScanner scanner;

    @BeforeMethod
    public void setUp() {
        scanner = new JaxrsApplicationAndResourcePackagesAnnotationScanner();
    }

    @Test(description = "scan classes from resource packages only")
    public void shouldScanOnlyResourcePackagesClasses() throws Exception {
        SwaggerConfiguration config = new SwaggerConfiguration()
                .resourcePackages(Stream.of("com.my.project.resources", "org.my.project.resources").collect(Collectors.toSet()))
                .openAPI(new OpenAPI().info(new Info().description("TEST INFO DESC")));

        OpenApiContext ctx = new GenericOpenApiContext<>()
                .openApiConfiguration(config)
                .openApiReader(new Reader(config))
                .openApiScanner(new JaxrsApplicationAndResourcePackagesAnnotationScanner().openApiConfiguration(config))
                .init();

        OpenAPI openApi = ctx.read();
        assertNotNull(openApi);
        assertEquals(openApi.getPaths().keySet(), Arrays.asList("/packageA", "/packageB"));
    }
    
    @Test(description = "scan classes from Application when it is not set")
    public void shouldScanForClassesWhenApplicationIsNotSet() throws Exception {
        SwaggerConfiguration config = new SwaggerConfiguration()
                .openAPI(new OpenAPI().info(new Info().description("TEST INFO DESC")));
        
        OpenApiContext ctx = new GenericOpenApiContext<>()
                .openApiConfiguration(config)
                .openApiReader(new Reader(config))
                .openApiScanner(scanner.openApiConfiguration(config))
                .init();

        OpenAPI openApi = ctx.read();

        assertNotNull(openApi);
        assertNull(openApi.getPaths());
    }
    
    @Test(description = "scan classes from Application")
    public void shouldScanOnlyApplicationClasses() throws Exception {
        SwaggerConfiguration config = new SwaggerConfiguration()
                .openAPI(new OpenAPI().info(new Info().description("TEST INFO DESC")));
        
        Application application = new Application() {
            @Override
            public Set<Class<?>> getClasses() {
                return Collections.singleton(ResourceInPackageA.class);
            }
        };
        
        OpenApiContext ctx = new GenericOpenApiContext<>()
                .openApiConfiguration(config)
                .openApiReader(new Reader(config))
                .openApiScanner(scanner.application(application).openApiConfiguration(config))
                .init();

        OpenAPI openApi = ctx.read();
        assertNotNull(openApi);
        assertEquals(openApi.getPaths().keySet(), Arrays.asList("/packageA"));
    }
}
