package io.swagger.oas.models;

import io.swagger.annotations.media.OASSchema;

public class Car {
    @OASSchema(readOnly = true)
    public Integer getWheelCount() {
        return new Integer(4);
    }

    public void setWheelCount(Integer wheelCount) {
        // does nothing
    }
}