package io.swagger.oas.integration.impl;

import io.swagger.oas.integration.OpenAPIConfiguration;
import io.swagger.oas.integration.ext.OpenApiConfigurationLoader;

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
