package io.swagger.models.composition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Shake hands with Thing1", parent = AbstractBaseModelWithSubTypes.class)
public class Thing1 extends AbstractBaseModelWithSubTypes {

    @ApiModelProperty(value = "Override the abstract a")
    public String a;
    @ApiModelProperty(value = "Thing1 has an additional field")
    public int x;
}
