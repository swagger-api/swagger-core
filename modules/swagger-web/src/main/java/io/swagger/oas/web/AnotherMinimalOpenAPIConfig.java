package io.swagger.oas.web;

import io.swagger.oas.models.OpenAPI;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface AnotherMinimalOpenAPIConfig {
    Set<String> getResourcePackages();

    Set<String> getResourceClasses();

    String getFilterClass();

    Collection<String> getIgnoredRoutes();

    OpenAPI getOpenAPI();

    Map<String, Object> getUserDefinedOptions();

    boolean isScanAllResources();

    boolean isPrettyPrint();
}