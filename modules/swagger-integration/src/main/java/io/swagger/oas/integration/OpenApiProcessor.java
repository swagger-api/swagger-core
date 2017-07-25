package io.swagger.oas.integration;

import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.web.OpenApiReader;
import io.swagger.oas.web.OpenApiScanner;

public interface OpenApiProcessor {

    String getId();

    OpenApiProcessor init();

    OpenAPI read();

    void setOpenApiScanner(OpenApiScanner openApiScanner);

    void setOpenApiReader(OpenApiReader openApiReader);

    OpenApiConfiguration getOpenApiConfiguration();

}
