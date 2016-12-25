package io.swagger.models;

import io.swagger.annotations.ApiModelProperty;

public class ModelWithBooleanProperty {
    @ApiModelProperty(allowableValues = "true")
    public Boolean isGreat;

    @ApiModelProperty(allowableValues = "1,2")
    public Integer intValue;
}
