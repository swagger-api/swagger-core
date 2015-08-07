package io.swagger.models.composition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Class that has a field that is the AbstractBaseModelWithSubTypes")
public class ModelWithFieldWithSubTypes {

    @ApiModelProperty(value = "Contained field with sub-types")
    AbstractBaseModelWithSubTypes z;

    public AbstractBaseModelWithSubTypes getZ() {
        return z;
    }

}
