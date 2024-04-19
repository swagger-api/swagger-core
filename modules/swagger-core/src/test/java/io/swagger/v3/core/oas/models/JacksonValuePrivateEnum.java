package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.Hidden;

/**
 * Enum holds values different from names.  Schema model will derive String value from jackson annotation JsonValue on private method.
 */
public enum JacksonValuePrivateEnum {
    FIRST("one"),
    SECOND("two"),
    THIRD("three"),
    @Hidden HIDDEN("hidden");

    private final String value;

    JacksonValuePrivateEnum(String value) {
        this.value = value;
    }

    @JsonValue
    private String getValue() {
        return value;
    }
}
