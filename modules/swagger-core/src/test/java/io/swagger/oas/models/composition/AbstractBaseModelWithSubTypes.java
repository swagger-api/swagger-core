package io.swagger.oas.models.composition;

import io.swagger.oas.annotations.media.Schema;

@Schema(description = "I am an Abstract Base Model with Sub-Types",
//        discriminator = "_type",
        anyOf = {Thing1.class, Thing2.class})
abstract public class AbstractBaseModelWithSubTypes {

    @Schema(description = "This value is used as a discriminator for serialization")
    public String _type;
    @Schema(description = "An arbitrary field")
    public String a;
    @Schema(description = "An arbitrary field")
    public String b;
}
