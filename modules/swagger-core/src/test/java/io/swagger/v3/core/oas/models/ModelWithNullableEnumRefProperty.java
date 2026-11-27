package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.annotation.Nullable;

public class ModelWithNullableEnumRefProperty {

    @Schema(enumAsRef = true)
    public TestSecondEnum enumAsRef;

    @Nullable
    @Schema(enumAsRef = true)
    public TestSecondEnum nullableEnumAsRef;

}
