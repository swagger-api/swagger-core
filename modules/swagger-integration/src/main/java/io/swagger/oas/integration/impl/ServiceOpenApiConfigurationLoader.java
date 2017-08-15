package io.swagger.oas.integration.impl;

import io.swagger.oas.integration.OpenAPIConfigBuilder;
import io.swagger.oas.integration.OpenAPIConfiguration;
import io.swagger.oas.integration.ext.OpenApiConfigurationLoader;

import java.io.IOException;
import java.util.ServiceLoader;

// TODO doesn't support multiple configs
public class ServiceOpenApiConfigurationLoader implements OpenApiConfigurationLoader {

    @Override
    public OpenAPIConfiguration load(String path)  throws IOException {

        ServiceLoader<OpenAPIConfigBuilder> loader = ServiceLoader.load(OpenAPIConfigBuilder.class);
        if (loader.iterator().hasNext()) {
            return loader.iterator().next().build(null);
        }
        throw new IOException("Error loading OpenAPIConfigBuilder service implementation.");
    }

    @Override
    public boolean exists(String path) {

        try {
            ServiceLoader<OpenAPIConfigBuilder> loader = ServiceLoader.load(OpenAPIConfigBuilder.class);
            if (loader.iterator().hasNext()) {
                loader.iterator().next();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
