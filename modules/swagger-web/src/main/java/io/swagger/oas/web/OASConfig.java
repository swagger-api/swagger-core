package io.swagger.oas.web;

import java.util.Map;
import java.util.Set;

import io.swagger.oas.models.OpenAPI;

public interface OASConfig {
    Class<?> getFilterClass();

    Set<String> getIgnoredClasses();

    OpenAPI getOpenAPI();

    Map<String, Object> getOptions();

    Set<String> getResourcePackages();

    boolean isScanDisabled();
}