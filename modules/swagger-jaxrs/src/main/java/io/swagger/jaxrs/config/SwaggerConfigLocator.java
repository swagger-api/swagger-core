package io.swagger.jaxrs.config;

import io.swagger.config.SwaggerConfig;
import io.swagger.models.Swagger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SwaggerConfigLocator {

    private static SwaggerConfigLocator instance;

    private ConcurrentMap<String, SwaggerConfig> configMap = new ConcurrentHashMap<String, SwaggerConfig>();
    private ConcurrentMap<String, Swagger> swaggerMap = new ConcurrentHashMap<String, Swagger>();

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
            public Swagger configure(Swagger swagger) {
                return swagger;
            }

            @Override
            public String getFilterClass() {
                return null;
            }
        };
    }

    public void putConfig(String id, SwaggerConfig config) {
        if (! configMap.containsKey(id)) configMap.put(id, config);
    }

    public Swagger getSwagger(String id) {
        Swagger value = swaggerMap.get(id);
        if (value != null) {
            return value;
        }
        return null;
    }

    public void putSwagger(String id, Swagger swagger) {
        if (! swaggerMap.containsKey(id)) swaggerMap.put(id, swagger);
    }

}
