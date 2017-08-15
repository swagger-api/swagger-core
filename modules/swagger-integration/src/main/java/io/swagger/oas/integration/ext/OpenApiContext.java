package io.swagger.oas.integration.ext;

import io.swagger.oas.integration.OpenAPIConfiguration;
import io.swagger.oas.integration.OpenAPIReader;
import io.swagger.oas.integration.OpenAPIScanner;
import io.swagger.oas.integration.impl.OpenApiConfigurationException;
import io.swagger.oas.models.OpenAPI;

public interface OpenApiContext {

    String OPENAPI_CONTEXT_ID_KEY = "openapi.context.id";
    String OPENAPI_CONTEXT_ID_PREFIX = OPENAPI_CONTEXT_ID_KEY + ".";
    String OPENAPI_CONTEXT_ID_DEFAULT = OPENAPI_CONTEXT_ID_PREFIX + "default";

    String getId();

    OpenApiContext init() throws OpenApiConfigurationException;

    OpenAPI read();

    OpenAPIConfiguration getOpenApiConfiguration();

    String getConfigLocation();

    OpenApiContext getParent();

    void setOpenApiScanner(OpenAPIScanner openApiScanner);

    void setOpenApiReader(OpenAPIReader openApiReader);

}
