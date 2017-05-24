package io.swagger.oas.models;

import io.swagger.oas.annotations.media.Schema;

public class ModelWithBooleanProperty {
    @Schema(_enum = "true")
    public Boolean isGreat;

    @Schema(_enum = {"1", "2"})
    public Integer intValue;
}
