package io.swagger.oas.models;

import io.swagger.oas.annotations.media.Schema;

public class ModelWithBooleanProperty {
    @Schema(allowableValues = "true")
    public Boolean isGreat;

    @Schema(allowableValues = {"1", "2"})
    public Integer intValue;
}
