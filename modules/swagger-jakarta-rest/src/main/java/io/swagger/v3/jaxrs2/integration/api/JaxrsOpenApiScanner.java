package io.swagger.v3.jaxrs2.integration.api;

import io.swagger.v3.oas.integration.api.OpenApiScanner;

import jakarta.ws.rs.core.Application;

public interface JaxrsOpenApiScanner extends OpenApiScanner {

    void setApplication(Application application);
}
