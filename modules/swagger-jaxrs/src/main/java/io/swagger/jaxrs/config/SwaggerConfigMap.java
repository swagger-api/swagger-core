package io.swagger.jaxrs.config;

import io.swagger.config.SwaggerConfig;
import io.swagger.models.Swagger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SwaggerConfigMap {

    private static SwaggerConfigMap instance;

    private ConcurrentMap<String, SwaggerConfig> map = new ConcurrentHashMap<String, SwaggerConfig>();

    public static SwaggerConfigMap getInstance() {
        if (instance == null) {
            instance = new SwaggerConfigMap();
        }
        return instance;
    }

    private SwaggerConfigMap() {
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
