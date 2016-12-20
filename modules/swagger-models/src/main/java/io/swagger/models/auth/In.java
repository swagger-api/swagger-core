package io.swagger.models.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.LinkedHashMap;
import java.util.Map;

public enum In {
    HEADER, QUERY;

    private static Map<String, In> names = new LinkedHashMap<String, In>();

    @JsonCreator
    public static In forValue(String value) {
        return names.get(value.toLowerCase());
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, In> entry : names.entrySet()) {
            if (entry.getValue() == this) {
                return entry.getKey();
            }
        }

        return null; // or fail
    }

    static {
        names.put("header", HEADER);
        names.put("query", QUERY);
    }
}
