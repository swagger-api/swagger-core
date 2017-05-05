package io.swagger.models.composition;

import io.swagger.annotations.media.OASSchema;

@OASSchema(description = "I am an Abstract Base Model with Sub-Types",
//        discriminator = "_type",
        anyOf = {Thing1.class, Thing2.class})
abstract public class AbstractBaseModelWithSubTypes {

    @OASSchema(description = "This value is used as a discriminator for serialization")
    public String _type;
    @OASSchema(description = "An arbitrary field")
    public String a;
    @OASSchema(description = "An arbitrary field")
    public String b;
}
