package io.swagger.v3.oas.integration;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface StringOpenApiConfigurationLoader extends OpenApiConfigurationLoader {

    Logger LOGGER = LoggerFactory.getLogger(StringOpenApiConfigurationLoader.class);

    default OpenAPIConfiguration deserializeConfig(String path, String configAsString) {

        try {
            if (path.toLowerCase().endsWith("json")) {
                return Json.mapper().readValue(configAsString, SwaggerConfiguration.class);
            } else { // assume yaml
                return Yaml.mapper().readValue(configAsString, SwaggerConfiguration.class);
            }

        } catch (Exception e) {
            LOGGER.error("exception reading config: " + e.getMessage(), e);
            return null;
        }

    }

}
