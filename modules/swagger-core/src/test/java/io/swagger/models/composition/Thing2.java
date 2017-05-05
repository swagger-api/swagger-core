package io.swagger.models.composition;

import io.swagger.annotations.media.OASSchema;

@OASSchema(description = "and Thing2"
//        , parent = AbstractBaseModelWithSubTypes.class
)
public class Thing2 extends AbstractBaseModelWithSubTypes {

    @OASSchema(description = "Override the abstract a")
    public String a;
    @OASSchema(description = "Thing2 has an additional field")
    public String s;
}
