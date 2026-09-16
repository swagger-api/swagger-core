package io.swagger.v3.jaxrs2.integration.servlet;

import jakarta.servlet.ServletContainerInitializer;
import org.testng.annotations.Test;

import java.util.ServiceLoader;

import static org.testng.Assert.assertTrue;

public class SwaggerServletInitializerTest {

    @Test
    public void shouldLoadInitializerFromJakartaServletServiceDescriptor() {
        boolean initializerFound = ServiceLoader.load(ServletContainerInitializer.class)
                .stream()
                .anyMatch(provider -> provider.type().equals(SwaggerServletInitializer.class));

        assertTrue(initializerFound);
    }
}
