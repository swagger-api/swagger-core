package io.swagger.oas.models.composition;

import io.swagger.oas.annotations.media.Schema;

@Schema(description = "Class that has a field that is the AbstractBaseModelWithSubTypes")
public class ModelWithFieldWithSubTypes {

    @Schema(description = "Contained field with sub-types")
    AbstractBaseModelWithSubTypes z;

    public AbstractBaseModelWithSubTypes getZ() {
        return z;
    }

}
