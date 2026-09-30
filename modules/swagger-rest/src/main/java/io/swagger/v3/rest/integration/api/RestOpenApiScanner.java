package io.swagger.v3.rest.integration.api;

import io.swagger.v3.oas.integration.api.OpenApiScanner;

import jakarta.ws.rs.core.Application;

public interface RestOpenApiScanner extends OpenApiScanner {

    void setApplication(Application application);
}
