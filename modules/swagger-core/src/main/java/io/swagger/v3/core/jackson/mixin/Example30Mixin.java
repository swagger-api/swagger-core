package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Mixin applied to {@link io.swagger.v3.oas.models.examples.Example} for the
 * OpenAPI 3.0 and 3.1 mappers: {@code dataValue} and {@code serializedValue} are
 * fixed fields of the Example Object only as of OpenAPI 3.2.
 */
public abstract class Example30Mixin extends ExampleMixin {

    @JsonIgnore
    public abstract Object getDataValue();

    @JsonIgnore
    public abstract String getSerializedValue();
}
