package io.swagger.v3.oas.integration.api;

import io.swagger.v3.oas.models.OpenAPI;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

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
