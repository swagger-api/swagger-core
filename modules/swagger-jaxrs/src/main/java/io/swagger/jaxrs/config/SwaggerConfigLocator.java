package io.swagger.jaxrs.config;

import io.swagger.config.SwaggerConfig;
import io.swagger.models.Swagger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SwaggerConfigLocator {

    private static SwaggerConfigLocator instance;

    private ConcurrentMap<String, SwaggerConfig> map = new ConcurrentHashMap<String, SwaggerConfig>();

    public static SwaggerConfigLocator getInstance() {
        if (instance == null) {
            instance = new SwaggerConfigLocator();
        }
        return instance;
    }

    private SwaggerConfigLocator() {
    }

    public SwaggerConfig getConfig(String id) {
        return map.getOrDefault(id, new SwaggerConfig() {
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
        map.putIfAbsent(id, config);
    }
}
