package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum holds values different from names.  Schema model will derive String value from jackson annotation JsonValue on private field.
 */
public enum JacksonValueFieldEnum {
    FIRST("one"),
    SECOND("two"),
    THIRD("three");

    @JsonValue
    private final String value;

    JacksonValueFieldEnum(String value) {
        this.value = value;
    }
}
