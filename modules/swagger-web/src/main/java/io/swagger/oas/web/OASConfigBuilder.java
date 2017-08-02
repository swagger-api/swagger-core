package io.swagger.oas.web;

import java.util.Map;

public interface OASConfigBuilder {
    OASConfig build(Map<String, Object> environment);
}