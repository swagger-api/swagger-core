package io.swagger.v3.core.oas.models;

/**
 * Generic interface whose {@code getValue()} method causes the compiler to generate
 * a bridge method on implementing enums. Used to test that bridge methods do not
 * interfere with {@code @JsonValue} detection.
 */
public interface PersistableEnum<T> {
    T getValue();
}
