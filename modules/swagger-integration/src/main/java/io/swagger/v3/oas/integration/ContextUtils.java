package io.swagger.v3.oas.integration;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(ContextUtils.class);

    // TODO implement proper clone see #2227
    public static OpenAPIConfiguration deepCopy (OpenAPIConfiguration config) {
        if (config == null) {
            return null;
        }
        try {
            return Json.mapper().readValue(Json.pretty(config), SwaggerConfiguration.class);
        } catch (Exception e) {
            LOGGER.error("Exception cloning config: " + e.getMessage(), e);
            return config;
        }
    }

}
