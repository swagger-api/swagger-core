package io.swagger.oas.integration;

import io.swagger.util.Json;
import io.swagger.util.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface StringOpenApiConfigurationLoader extends OpenApiConfigurationLoader {

    Logger LOGGER = LoggerFactory.getLogger(StringOpenApiConfigurationLoader.class);

    default OpenApiConfiguration deserializeConfig(String path, String configAsString) {

        try {
            if (path.toLowerCase().endsWith("json")) {
                return Json.mapper().readValue(configAsString, OpenApiConfiguration.class);
            } else { // assume yaml
                return Yaml.mapper().readValue(configAsString, OpenApiConfiguration.class);
            }

        } catch (Exception e) {
            LOGGER.error("exception reading config: " + e.getMessage(), e);
            return null;
        }

    }

}
