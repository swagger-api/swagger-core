package io.swagger.models;

import io.swagger.annotations.ApiModelProperty;

public class ModelWithReferences {
    public String getOne() {
        return null;
    }

    @ApiModelProperty(reference = "http://swagger.io/schemas.json#/Models/AnotherModel")
    public String getAnother() {
        return null;
    }

    @ApiModelProperty(reference = "Three")
    public String getThree() {
        return null;
    }

    @ApiModelProperty(reference = "Four.json")
    public String getFour() {
        return null;
    }

    @ApiModelProperty(reference = "Five.json.MyClass")
    public String getFive() {
        return null;
    }

    @ApiModelProperty(reference = "./Six")
    public String getSix() {
        return null;
    }
}
