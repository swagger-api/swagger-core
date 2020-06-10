package io.swagger.v3.oas.integration.api;

import io.swagger.v3.oas.integration.OpenApiConfigurationException;

public interface OpenApiContextBuilder {

    OpenApiContext buildContext(boolean init) throws OpenApiConfigurationException;
}
