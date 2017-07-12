package io.swagger.oas.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import io.swagger.oas.models.OpenAPI;

public interface OpenAPIConfig {
    Set<Class<?>> getClasses();

    Class<?> getFilterClass();

    Collection<String> getIgnoredRoutes();

    OpenAPI getOpenAPI();

    Map<String, Object> getUserDefinedOptions();

    boolean isPrettyPrint();

    boolean isScanAllResources();
}