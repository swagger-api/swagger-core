package io.swagger.oas.web;

import io.swagger.oas.models.OpenAPI;

import java.util.Map;
import java.util.Set;

public interface OpenApiReader {

    void setConfiguration(OpenAPIConfig openApiConfiguration);

    OpenAPI read(Set<Class<?>> classes, Map<String, Object> resources);
}
