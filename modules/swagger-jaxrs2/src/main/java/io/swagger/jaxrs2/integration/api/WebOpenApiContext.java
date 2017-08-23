package io.swagger.jaxrs2.integration.api;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import io.swagger.oas.integration.ext.OpenApiContext;

public interface WebOpenApiContext extends OpenApiContext {

    ServletContext getServletContext();
    ServletConfig getServletConfig();


}
