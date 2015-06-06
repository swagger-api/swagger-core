package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Scheme {
    HTTP("http"),
    HTTPS("https"),
    WS("ws"),
    WSS("wss");

    private final String value;

    private Scheme(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Scheme forValue(String value) {
        for (Scheme item : Scheme.values()) {
            if (item.toValue().equalsIgnoreCase(value)) {
                return item;
            }
        }
        return null;
    }

    @JsonValue
    public String toValue() {
        return value;
    }
}