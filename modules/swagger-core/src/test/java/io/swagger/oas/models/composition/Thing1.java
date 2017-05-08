package io.swagger.oas.models.composition;

import io.swagger.annotations.media.OASSchema;

@OASSchema(description = "Shake hands with Thing1"
//        , parent = AbstractBaseModelWithSubTypes.class
)
public class Thing1 extends AbstractBaseModelWithSubTypes {

    @OASSchema(description = "Override the abstract a")
    public String a;
    @OASSchema(description = "Thing1 has an additional field")
    public int x;
}
