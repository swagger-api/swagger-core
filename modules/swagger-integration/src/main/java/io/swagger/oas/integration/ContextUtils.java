package io.swagger.oas.integration;

import io.swagger.oas.web.OpenAPIConfig;
import io.swagger.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(ContextUtils.class);

    public static OpenApiConfiguration deepCopy (OpenApiConfiguration config) {
        if (config == null) {
            return null;
        }
        try {
            return Json.mapper().readValue(Json.pretty(config), OpenApiConfiguration.class);
        } catch (Exception e) {
            LOGGER.error("Exception cloning config: " + e.getMessage(), e);
            return config;
        }
    }

    public static OpenApiConfiguration cloneConfigFromInterface(OpenAPIConfig configInterface) {

        return new OpenApiConfiguration()
                .openApi(configInterface.getOpenAPI())
                .userDefinedOptions(configInterface.getUserDefinedOptions())
                .filterClass(configInterface.getFilterClass())
                .prettyPrint(configInterface.isPrettyPrint())
                .readerClass(configInterface.getReaderClass())
                .resourcePackages(configInterface.getResourcePackages())
                .resourceClasses(configInterface.getResourceClasses())
                .scanAllResources(configInterface.isScanAllResources())
                .scannerClass(configInterface.getScannerClass());
    }

}
