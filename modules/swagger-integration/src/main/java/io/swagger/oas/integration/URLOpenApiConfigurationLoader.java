package io.swagger.oas.integration;

import io.swagger.oas.integration.api.OpenAPIConfiguration;
import io.swagger.oas.integration.api.OpenApiConfigurationLoader;

import java.io.IOException;

// TODO
public class URLOpenApiConfigurationLoader implements OpenApiConfigurationLoader {

    @Override
    public OpenAPIConfiguration load(String path)  throws IOException {
        return null;
    }

    @Override
    public boolean exists(String path) {
        return false;
    }
}
