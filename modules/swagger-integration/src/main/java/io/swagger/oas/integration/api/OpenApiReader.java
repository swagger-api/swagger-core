package io.swagger.oas.integration.api;

import io.swagger.oas.models.OpenAPI;

import java.util.Map;
import java.util.Set;

public interface OpenApiReader {

    void setConfiguration(OpenAPIConfiguration openApiConfiguration);

    OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources);
}
