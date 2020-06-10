package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class ModelWithBooleanProperty {
    @Schema(allowableValues = "true")
    public Boolean isGreat;

    @Schema(allowableValues = {"1", "2"})
    public Integer intValue;
}
