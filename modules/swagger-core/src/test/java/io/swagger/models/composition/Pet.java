package io.swagger.models.composition;

import io.swagger.annotations.ApiModelProperty;

public interface Pet extends Animal {
    @ApiModelProperty(position = 1, required = true, value = "The pet type")
    String getType();

    void setType(String type);

    @ApiModelProperty(position = 2, required = true, value = "The name of the pet")
    String getName();

    void setName(String name);

    @ApiModelProperty(position = 3, required = true)
    Boolean getIsDomestic();

    void setIsDomestic(Boolean isDomestic);
}
