package io.swagger.oas.models.composition;

import io.swagger.annotations.media.OASSchema;

@OASSchema(description = "Class that has a field that is the AbstractBaseModelWithSubTypes")
public class ModelWithFieldWithSubTypes {

    @OASSchema(description = "Contained field with sub-types")
    AbstractBaseModelWithSubTypes z;

    public AbstractBaseModelWithSubTypes getZ() {
        return z;
    }

}
