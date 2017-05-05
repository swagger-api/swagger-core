package io.swagger.models;

import io.swagger.annotations.media.OASSchema;

public class ModelContainingModelWithReference {
    public ModelWithReference getModel() {
        return null;
    }

    @OASSchema(description = "http://swagger.io/schemas.json#/Models/AnotherModel")
    public ModelWithReference getAnotherModel() {
        return null;
    }
}
