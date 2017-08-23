package io.swagger.oas.integration;

import java.util.Map;

public interface OpenAPIConfigurationBuilder {
    OpenAPIConfiguration build(Map<String, Object> environment);
}