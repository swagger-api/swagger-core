package io.swagger.models;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JacksonValueEnum {
    FIRST("one"),
    SECOND("two"),
    THIRD("three");

    private final String value;

    JacksonValueEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}