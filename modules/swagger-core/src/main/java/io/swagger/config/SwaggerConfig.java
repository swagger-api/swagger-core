package io.swagger.config;

import io.swagger.models.Swagger;

public interface SwaggerConfig {

    public static final String CONFIG_ID_KEY = "swagger.config.id";
    public static final String CONFIG_ID_PREFIX = CONFIG_ID_KEY + ".";
    public static final String CONFIG_ID_DEFAULT = CONFIG_ID_PREFIX + "default";

    Swagger configure(Swagger swagger);

    String getFilterClass();
}