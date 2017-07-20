package io.swagger.jaxrs2.integration;

import io.swagger.oas.integration.OpenApiContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public interface WebOpenApiContext extends OpenApiContext {

    ServletContext getServletContext();
    ServletConfig getServletConfig();


}
