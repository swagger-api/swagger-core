package io.swagger.oas.models;

import io.swagger.oas.annotations.media.Schema;

public class ModelContainingModelWithReference {
    public ModelWithReference getModel() {
        return null;
    }

    @Schema(description = "http://swagger.io/schemas.json#/Models/AnotherModel")
    public ModelWithReference getAnotherModel() {
        return null;
    }
}
