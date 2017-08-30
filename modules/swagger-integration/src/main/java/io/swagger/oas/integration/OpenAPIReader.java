package io.swagger.oas.integration;

import java.util.Map;
import java.util.Set;

import io.swagger.oas.models.OpenAPI;

public interface OpenAPIReader {
    void setConfiguration(OpenAPIConfiguration openAPIConfiguration);

    OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources);
}
