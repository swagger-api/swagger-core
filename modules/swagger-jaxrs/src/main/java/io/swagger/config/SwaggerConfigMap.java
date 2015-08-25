package io.swagger.config;

import java.util.Map;
import java.util.HashMap;

public class SwaggerConfigMap {
    private static Map<String, SwaggerConfig> map = new HashMap<String, SwaggerConfig>();

    public static SwaggerConfig getConfig(String id) {
        return SwaggerConfigMap.map.get(id);
    }
    
    public static void storeConfig(String id, SwaggerConfig config) {
        SwaggerConfigMap.map.put(id, config);
    }
}
