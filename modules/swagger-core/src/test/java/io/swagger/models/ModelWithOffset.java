package io.swagger.models;

import io.swagger.annotations.ApiModelProperty;

public class ModelWithOffset {
    public String id;

    @ApiModelProperty(dataType = "java.time.OffsetDateTime")
    public String offset;
}
