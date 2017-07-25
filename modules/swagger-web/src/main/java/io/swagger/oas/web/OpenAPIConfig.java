package io.swagger.oas.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import io.swagger.oas.models.OpenAPI;

public interface OpenAPIConfig {
    Set<String> getResourcePackages();

    Set<Class<?>> getResourceClasses();

    Class<OpenApiReader> getReaderClass();

    Class<OpenApiScanner> getScannerClass();

    // TODO use updated/renamed SwaggerSpecFilter, moved to swagger-web
    Class<?> getFilterClass();

    Collection<String> getIgnoredRoutes();

    OpenAPI getOpenAPI();

    Map<String, Object> getUserDefinedOptions();

    boolean isScanAllResources();

    boolean isPrettyPrint();
}