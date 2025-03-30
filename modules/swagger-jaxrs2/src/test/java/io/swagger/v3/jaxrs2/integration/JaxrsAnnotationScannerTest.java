package io.swagger.v3.jaxrs2.integration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.my.project.resources.ResourceInPackageB;
import org.testng.annotations.Test;

import com.my.project.resources.ResourceInPackageA;

import io.swagger.v3.oas.integration.SwaggerConfiguration;
import net.my.project.resources.Resource2InPackageC;
import net.my.project.resources.ResourceInPackageC;

public class JaxrsAnnotationScannerTest {
    
    @Test(description = "resource classes and resource packages should work at the same time")
    public void shouldScanClassesAndPackages() throws Exception {
        SwaggerConfiguration config = new SwaggerConfiguration()
                .resourcePackages(Stream.of("com.my.project.resources", "org.my.project.resources").collect(Collectors.toSet()))
                .resourceClasses(Stream.of("net.my.project.resources.ResourceInPackageC").collect(Collectors.toSet()));

        JaxrsAnnotationScanner<?> classUnderTest = new JaxrsAnnotationScanner<>();
    	classUnderTest.setConfiguration(config);
    	
    	Set<Class<?>> result = classUnderTest.classes();

        assertNotNull(result);
        assertEquals(result.size(), 3);
        assertTrue(result.contains(ResourceInPackageA.class));
        assertTrue(result.contains(ResourceInPackageB.class));
        assertTrue(result.contains(ResourceInPackageC.class));
        assertFalse(result.contains(Resource2InPackageC.class));
    }

}
