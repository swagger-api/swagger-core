package io.swagger.oas.integration;


import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.web.OpenApiReader;
import io.swagger.oas.web.OpenApiScanner;

public interface OpenApiContext {

    String OPENAPI_CONTEXT_ID_KEY = "openapi.context.id";
    String OPENAPI_CONTEXT_ID_PREFIX = OPENAPI_CONTEXT_ID_KEY + ".";
    String OPENAPI_CONTEXT_ID_DEFAULT = OPENAPI_CONTEXT_ID_PREFIX + "default";

    String getId();

    OpenApiContext init() throws OpenApiConfigurationException;

    OpenAPI read();

    OpenApiConfiguration getOpenApiConfiguration();

    String getConfigLocation();

    OpenApiContext getParent();

    void setOpenApiScanner(OpenApiScanner openApiScanner);

    void setOpenApiReader(OpenApiReader openApiReader);

}
