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
        return configMap.getOrDefault(id, new SwaggerConfig() {
            @Override
            public Swagger configure(Swagger swagger) {
                return swagger;
            }

            @Override
            public String getFilterClass() {
                return null;
            }
        });
    }

    public void putConfig(String id, SwaggerConfig config) {
        configMap.putIfAbsent(id, config);
    }

    public Swagger getSwagger(String id) {
        return swaggerMap.getOrDefault(id, new Swagger());
    }

    public void putSwagger(String id, Swagger swagger) {
        swaggerMap.putIfAbsent(id, swagger);
    }

}
