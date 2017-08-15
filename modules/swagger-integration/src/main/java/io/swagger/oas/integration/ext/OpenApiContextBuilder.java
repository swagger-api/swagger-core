package io.swagger.oas.integration.ext;

import io.swagger.oas.integration.impl.OpenApiConfigurationException;

public interface OpenApiContextBuilder {

    OpenApiContext buildContext(boolean init) throws OpenApiConfigurationException;
}
