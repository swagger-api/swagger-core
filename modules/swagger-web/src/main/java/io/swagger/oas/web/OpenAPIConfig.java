package io.swagger.oas.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import io.swagger.oas.models.OpenAPI;

public interface OpenAPIConfig {
    Set<String> getResourcePackages();

    Set<String> getResourceClasses();

    String getReaderClass();

    String getScannerClass();

    String getFilterClass();

    Collection<String> getIgnoredRoutes();

    OpenAPI getOpenAPI();

    Map<String, Object> getUserDefinedOptions();

    Boolean isScanAllResources();

    Boolean isPrettyPrint();
}