package io.swagger.jaxrs2.config;

import io.swagger.config.SwaggerConfig;
import io.swagger.oas.models.OpenAPI;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SwaggerConfigLocator {

    private static SwaggerConfigLocator instance;

    private ConcurrentMap<String, SwaggerConfig> configMap = new ConcurrentHashMap<String, SwaggerConfig>();
    private ConcurrentMap<String, OpenAPI> openAPIMap = new ConcurrentHashMap<String, OpenAPI>();

    public static SwaggerConfigLocator getInstance() {
        if (instance == null) {
            instance = new SwaggerConfigLocator();
        }
        return instance;
    }

    private SwaggerConfigLocator() {
    }

    public SwaggerConfig getConfig(String id) {
        SwaggerConfig value = configMap.get(id);
        if (value != null) {
            return value;
        }
        return new SwaggerConfig() {
            @Override
            public OpenAPI configure(OpenAPI openAPI) {
                return openAPI;
            }

            @Override
            public String getFilterClass() {
                return null;
            }
        };
    }

    public void putConfig(String id, SwaggerConfig config) {
        if (!configMap.containsKey(id)) configMap.put(id, config);
    }

    public OpenAPI getSwagger(String id) {
        OpenAPI value = openAPIMap.get(id);
        if (value != null) {
            return value;
        }
        return null;
    }

    public void putSwagger(String id, OpenAPI openAPI) {
        if (!openAPIMap.containsKey(id)) openAPIMap.put(id, openAPI);
    }

}
