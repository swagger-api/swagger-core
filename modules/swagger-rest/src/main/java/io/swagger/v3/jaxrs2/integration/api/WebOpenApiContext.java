package io.swagger.v3.jaxrs2.integration.api;

import io.swagger.v3.oas.integration.api.OpenApiContext;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;

public interface WebOpenApiContext extends OpenApiContext {

    ServletContext getServletContext();

    ServletConfig getServletConfig();

}
