package io.swagger.models;

import io.swagger.annotations.ApiModelProperty;

public class Car {
    @ApiModelProperty(readOnly = true)
    public Integer getWheelCount() {
        return new Integer(4);
    }

    public void setWheelCount(Integer wheelCount) {
        // does nothing
    }
}