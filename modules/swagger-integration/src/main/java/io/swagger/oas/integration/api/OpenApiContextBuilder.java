package io.swagger.oas.integration.api;

import io.swagger.oas.integration.OpenApiConfigurationException;

public interface OpenApiContextBuilder {

    OpenApiContext buildContext(boolean init) throws OpenApiConfigurationException;
}
