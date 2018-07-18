package io.swagger.v3.oas.integration;

import io.swagger.v3.oas.integration.api.OpenApiContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OpenApiContextLocator {

    private static OpenApiContextLocator instance;

    private ConcurrentMap<String, OpenApiContext> map = new ConcurrentHashMap<String, OpenApiContext>();

    private OpenApiContextLocator() {
    }

    public static OpenApiContextLocator getInstance() {
        if (instance == null) {
            instance = new OpenApiContextLocator();
        }
        return instance;
    }

    public OpenApiContext getOpenApiContext(String id) {
        return map.get(id);
    }

    public void putOpenApiContext(String id, OpenApiContext openApiContext) {
        map.put(id, openApiContext);
    }
}
