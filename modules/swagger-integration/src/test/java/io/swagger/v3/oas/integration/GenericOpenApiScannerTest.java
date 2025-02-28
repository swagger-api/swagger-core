package io.swagger.v3.oas.integration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.annotations.Test;

import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.fixture.ClassA;
import io.swagger.v3.oas.integration.fixture.ClassB;
import io.swagger.v3.oas.integration.fixture.test.ClassC;

public class GenericOpenApiScannerTest {

    @Test(description = "resourceClasses and resourcePackages should work at the same time")
    public void shouldReadClassesAndPackages() throws Exception {

        OpenAPIConfiguration config = new SwaggerConfiguration()
                .resourcePackages(Stream.of("io.swagger.v3.oas.integration.fixture.test").collect(Collectors.toSet()))
                .resourceClasses(Stream.of("io.swagger.v3.oas.integration.fixture.ClassB").collect(Collectors.toSet()));
    	GenericOpenApiScanner classUnderTest = new GenericOpenApiScanner();
    	classUnderTest.setConfiguration(config);
    	
    	Set<Class<?>> result = classUnderTest.classes();

        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertTrue(result.contains(ClassB.class));
        assertTrue(result.contains(ClassC.class));
        assertFalse(result.contains(ClassA.class));
    }

}
