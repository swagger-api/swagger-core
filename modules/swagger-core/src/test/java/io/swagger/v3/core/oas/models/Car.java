package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class Car {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Integer getWheelCount() {
        return new Integer(4);
    }

    public void setWheelCount(Integer wheelCount) {
        // does nothing
    }
}