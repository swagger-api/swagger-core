package io.swagger.oas.integration;

import java.util.Map;

public interface OpenAPIConfigBuilder {
    OpenAPIConfiguration build(Map<String, Object> environment);
}