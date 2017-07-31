package io.swagger.oas.integration;

public interface OpenApiContextBuilder {

    OpenApiContext buildContext(boolean init) throws OpenApiConfigurationException;
}
