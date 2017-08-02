package io.swagger.oas.integration.api;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import io.swagger.oas.models.OpenAPI;

public interface OpenAPIConfiguration {
    Set<String> getResourcePackages();

    Set<String> getResourceClasses();

    String getReaderClass();

    String getScannerClass();

    String getFilterClass();

    Collection<String> getIgnoredRoutes();

    OpenAPI getOpenAPI();

    Map<String, Object> getUserDefinedOptions();

    Boolean isReadAllResources();

    Boolean isPrettyPrint();

    Long getCacheTTL();
}
