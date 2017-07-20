package io.swagger.oas.integration;


import io.swagger.oas.models.OpenAPI;

import java.util.Map;

public interface OpenApiContext {

    // TODO move to constants
    String OPENAPI_CONTEXT_ID_KEY = "openapi.context.id";
    String OPENAPI_CONTEXT_ID_PREFIX = OPENAPI_CONTEXT_ID_KEY + ".";
    String OPENAPI_CONTEXT_ID_DEFAULT = OPENAPI_CONTEXT_ID_PREFIX + "default";

    Map<String, OpenApiProcessor> getOpenApiProcessors();

    String getId();

    OpenApiContext init();

    OpenAPI read();

    OpenApiConfiguration getOpenApiConfiguration();

    String getConfigLocation();

    OpenApiProcessor getDefaultProcessor();

    OpenApiContext getParent();
}
