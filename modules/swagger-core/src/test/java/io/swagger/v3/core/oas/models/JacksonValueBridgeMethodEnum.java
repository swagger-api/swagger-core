package io.swagger.v3.core.oas.models;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum implementing a generic interface, causing the compiler to generate a bridge
 * method {@code Object getValue()} alongside the real {@code String getValue()}.
 * The {@code @JsonValue} annotation is present on the real method, but the compiler
 * may copy it to the bridge method as well. This tests that the bridge method does
 * not interfere with schema type detection.
 *
 * @see <a href="https://github.com/swagger-api/swagger-core/issues/5127">Issue #5127</a>
 */
public enum JacksonValueBridgeMethodEnum implements PersistableEnum<String> {
    CREATED("created"),
    IN_PROGRESS("in_progress"),
    CONFIRMED("confirmed");

    private final String value;

    JacksonValueBridgeMethodEnum(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
