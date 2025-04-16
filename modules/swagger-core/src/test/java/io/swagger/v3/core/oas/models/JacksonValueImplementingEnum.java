package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.Hidden;

/**
 * Enum holds values different from names.
 * Schema model will derive String value from jackson annotation JsonValue on public implemented method.
 */
public enum JacksonValueImplementingEnum implements IEnum {
    FIRST("one"),
    SECOND("two"),
    THIRD("three"),
    @Hidden HIDDEN("hidden");

    private final String value;

    JacksonValueImplementingEnum(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
