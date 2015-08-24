package io.swagger.model.override;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GenericModel {
    private static final Map<String, Class<?>> additionalPropertyTypes = new HashMap<String, Class<?>>();
    private Map<String, Object> additionalProperties;

    public static void declareProperty(String key, Class<?> cls) {
        additionalPropertyTypes.put(key, cls);
    }

    public static Map<String, Class<?>> getDeclaredProperties() {
        return Collections.unmodifiableMap(additionalPropertyTypes);
    }

    public static void undeclareProperty(String key) {
        additionalPropertyTypes.remove(key);
    }

    public void setProperty(String key, Object value) {
        if (value == null || additionalPropertyTypes.get(key).isAssignableFrom(value.getClass())) {
            additionalProperties.put(key, value);
        }
    }

    public Object getProperty(String key) {
        return additionalProperties.get(key);
    }

}
