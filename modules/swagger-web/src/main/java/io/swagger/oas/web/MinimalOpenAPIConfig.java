package io.swagger.oas.web;

import io.swagger.oas.models.OpenAPI;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface MinimalOpenAPIConfig {

    Set<String> getResourcePackages();

    Set<Class<?>> getResourceClasses();

    // TODO use updated/renamed SwaggerSpecFilter, moved to swagger-web
    Class<?> getFilterClass();

    Collection<String> getIgnoredRoutes();

    OpenAPI getOpenAPI();

    Map<String, Object> getUserDefinedOptions();

    boolean isScanAllResources();

    boolean isPrettyPrint();
}