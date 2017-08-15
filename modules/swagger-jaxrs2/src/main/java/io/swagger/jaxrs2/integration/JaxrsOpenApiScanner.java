package io.swagger.jaxrs2.integration;

import javax.ws.rs.core.Application;

import io.swagger.oas.integration.OpenAPIScanner;

import java.util.Map;

public interface JaxrsOpenApiScanner extends OpenAPIScanner{


    void setApplication (Application application);

    Map<String, Object> resources();
}
