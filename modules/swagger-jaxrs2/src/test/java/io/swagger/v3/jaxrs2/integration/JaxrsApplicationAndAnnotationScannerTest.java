package io.swagger.v3.jaxrs2.integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.my.project.resources.ResourceInPackageB;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.my.project.resources.ResourceInPackageA;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.integration.SwaggerConfiguration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

public class JaxrsApplicationAndAnnotationScannerTest {
    private JaxrsApplicationAndAnnotationScanner scanner;

    @Path("/app")
    public static class ResourceInApplication {
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
    
    @Test(description = "scan classes from Application")
    public void shouldOnlyScanClassesFromApplication() throws Exception {
        assertEquals(scanner.classes().size(), 1);
        assertTrue(scanner.classes().contains(ResourceInApplication.class));
    }
    
    @Test(description = "scan classes from all packages and Application")
    public void shouldOnlyScanClassesFromAllPackagesAndApplication() throws Exception {
        SwaggerConfiguration openApiConfiguration = new SwaggerConfiguration();
        openApiConfiguration.setResourcePackages(Collections.singleton("*"));
        scanner.setConfiguration(openApiConfiguration);
        
        assertNotEquals(scanner.classes().size(), 1);
        assertTrue(scanner.classes().contains(ResourceInPackageA.class));
        assertTrue(scanner.classes().contains(ResourceInPackageB.class));
        assertTrue(scanner.classes().contains(ResourceInApplication.class));
    }
    
    @Test(description = "scan classes from the packages and Application")
    public void shouldOnlyScanClassesFromPackagesAndApplication() throws Exception {
        SwaggerConfiguration openApiConfiguration = new SwaggerConfiguration();
        openApiConfiguration.setResourcePackages(Collections.singleton("com.my.project.resources"));
        scanner.setConfiguration(openApiConfiguration);
        
        assertEquals(scanner.classes().size(), 2);
        assertTrue(scanner.classes().contains(ResourceInPackageA.class));
        assertTrue(scanner.classes().contains(ResourceInApplication.class));
    }
}
