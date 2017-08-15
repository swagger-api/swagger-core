package io.swagger.oas.integration.impl;

import java.io.IOException;

import io.swagger.oas.integration.OpenAPIConfiguration;

public class ClasspathOpenApiConfigurationLoader implements StringOpenApiConfigurationLoader {

    @Override
    public OpenAPIConfiguration load(String path)  throws IOException {
        String sanitized = (path.startsWith("/") ? path : "/" + path);
        String configString = readInputStreamToString(this.getClass().getResource(sanitized).openStream());
        return deserializeConfig(path, configString);

    }

    @Override
    public boolean exists(String path) {
        String sanitized = (path.startsWith("/") ? path : "/" + path);
        return this.getClass().getResource(sanitized) != null;
    }
}
