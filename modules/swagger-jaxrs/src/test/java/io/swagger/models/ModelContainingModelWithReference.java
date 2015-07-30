package io.swagger.models;

import io.swagger.annotations.ApiModelProperty;

public class ModelContainingModelWithReference {
    public ModelWithReference getModel() {
        return null;
    }

    @ApiModelProperty(reference = "http://swagger.io/schemas.json#/Models/AnotherModel")
    public ModelWithReference getAnotherModel() {
        return null;
    }
}
