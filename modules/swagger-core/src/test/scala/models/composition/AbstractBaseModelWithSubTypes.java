package models.composition;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(description = "I am an Abstract Base Model with Sub-Types",
        discriminator = "_type",
        subTypes = {Thing1.class, Thing2.class})
abstract public class AbstractBaseModelWithSubTypes {

    @ApiModelProperty(value = "This value is used as a discriminator for serialization") public String _type;
    @ApiModelProperty(value = "An arbitrary field") public String a;
    @ApiModelProperty(value = "An arbitrary field") public String b;
}
