package io.swagger.oas.web;

import java.util.Map;

public interface OpenAPIController {
    OpenAPIConfig bootstrap(Map<String, Object> environment);
}