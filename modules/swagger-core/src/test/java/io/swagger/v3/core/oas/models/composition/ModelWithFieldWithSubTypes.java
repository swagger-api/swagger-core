package io.swagger.v3.core.oas.models.composition;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Class that has a field that is the AbstractBaseModelWithSubTypes")
public class ModelWithFieldWithSubTypes {

    // TODO #2312 this annotation is not picked in property, as for ref properties, only ref is added. is this matching spec?
    @Schema(description = "Contained field with sub-types")
    AbstractBaseModelWithSubTypes z;

    public AbstractBaseModelWithSubTypes getZ() {
        return z;
    }

}
