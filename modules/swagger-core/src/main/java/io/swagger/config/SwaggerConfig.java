package io.swagger.config;

import io.swagger.models.OpenAPI;

public interface SwaggerConfig {

    OpenAPI configure(OpenAPI oai);

    String getFilterClass();
}