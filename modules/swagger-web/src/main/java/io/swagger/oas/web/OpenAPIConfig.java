package io.swagger.oas.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import io.swagger.oas.models.OpenAPI;

public interface OpenAPIConfig {
    Set<String> getResources();

    Class<?> getFilterClass();

    Collection<String> getIgnoredRoutes();

    OpenAPI getOpenAPI();

    Map<String, Object> getUserDefinedOptions();

    boolean isScanAllResources();
}