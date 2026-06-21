package io.swagger.v3.jakartarest.integration.api;

import io.swagger.v3.oas.integration.api.OpenApiContext;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;

public interface WebOpenApiContext extends OpenApiContext {

    ServletContext getServletContext();

    ServletConfig getServletConfig();

}
