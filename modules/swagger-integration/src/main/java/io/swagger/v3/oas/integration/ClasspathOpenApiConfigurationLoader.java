package io.swagger.v3.oas.integration;

import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;

import java.io.IOException;

public class ClasspathOpenApiConfigurationLoader implements StringOpenApiConfigurationLoader {

    @Override
    public OpenAPIConfiguration load(String path) throws IOException {
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
