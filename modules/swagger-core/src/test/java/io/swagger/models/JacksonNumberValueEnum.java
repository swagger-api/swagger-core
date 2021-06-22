package io.swagger.models;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JacksonNumberValueEnum {
    FIRST(2),
    SECOND(4),
    THIRD(6);

    private final int value;

    JacksonNumberValueEnum(int value) {
        this.value = value;
    }

    @JsonValue
    public Number getValue() {
        return value;
    }
}