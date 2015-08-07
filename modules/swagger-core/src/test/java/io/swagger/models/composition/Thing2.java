package io.swagger.models.composition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "and Thing2", parent = AbstractBaseModelWithSubTypes.class)
public class Thing2 extends AbstractBaseModelWithSubTypes {

    @ApiModelProperty(value = "Override the abstract a")
    public String a;
    @ApiModelProperty(value = "Thing2 has an additional field")
    public String s;
}
