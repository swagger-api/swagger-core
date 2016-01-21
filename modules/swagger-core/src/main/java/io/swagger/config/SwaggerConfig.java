package io.swagger.config;

import io.swagger.models.Swagger;

public interface SwaggerConfig {

    Swagger configure(Swagger swagger);

    String getFilterClass();
}