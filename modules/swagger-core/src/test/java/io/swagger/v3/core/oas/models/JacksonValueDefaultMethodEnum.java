package io.swagger.v3.core.oas.models;

/**
 * Enum whose @JsonValue comes from a default method declared on the implemented interface.
 * The enum overrides the method (without repeating @JsonValue), so the annotation is only
 * present on the interface's default method declaration.
 */
public enum JacksonValueDefaultMethodEnum implements InterfaceWithDefaultJsonValue {
    ALPHA, BETA, GAMMA;

    @Override
    public String toValue() {
        return this.name().toLowerCase();
    }
}
