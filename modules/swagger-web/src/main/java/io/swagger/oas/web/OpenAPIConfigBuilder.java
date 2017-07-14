package io.swagger.oas.web;

import java.util.Map;

public interface OpenAPIConfigBuilder {
    OpenAPIConfig build(Map<String, Object> environment);
}