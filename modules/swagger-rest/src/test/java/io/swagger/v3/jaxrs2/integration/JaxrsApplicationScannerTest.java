package io.swagger.v3.jaxrs2.integration;

import java.util.Collections;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.my.project.resources.ResourceInPackageA;

import io.swagger.v3.oas.integration.SwaggerConfiguration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class JaxrsApplicationScannerTest {
    private JaxrsApplicationScanner scanner;

    @BeforeMethod
    public void setUp() {
        scanner = new JaxrsApplicationScanner();
        
        scanner.setApplication(new Application() {
            @Override
            public Set<Class<?>> getClasses() {
                return Collections.singleton(ResourceInPackageA.class);
            }
        });
    }
    
    @Test(description = "scan classes from Application only")
    public void shouldScanClassesApplicationOnly() throws Exception {
        assertEquals(scanner.classes().size(), 1);
        assertTrue(scanner.classes().contains(ResourceInPackageA.class));
    }
    
    @Test(description = "scan classes from the Application only, ignoring resource packages")
    public void shouldScanClassesFromApplicationOnlyIgnoringResourcePackages() throws Exception {
        SwaggerConfiguration openApiConfiguration = new SwaggerConfiguration();
        openApiConfiguration.setResourcePackages(Collections.singleton("com.my.project.resources"));
        scanner.setConfiguration(openApiConfiguration);
        
        assertEquals(scanner.classes().size(), 1);
        assertTrue(scanner.classes().contains(ResourceInPackageA.class));
    }
    
    @Test(description = "scan classes from Application when is not set")
    public void shouldScanForClassesWhenApplicationNotSet() throws Exception {
        scanner.application(null);
        assertTrue(scanner.classes().isEmpty());
    }

}
