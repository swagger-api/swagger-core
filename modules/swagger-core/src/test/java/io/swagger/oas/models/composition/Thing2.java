package io.swagger.oas.models.composition;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "and Thing2", allOf = {AbstractBaseModelWithSubTypes.class})
public class Thing2 extends AbstractBaseModelWithSubTypes {

    @Schema(description = "Override the abstract a")
    public String a;
    @Schema(description = "Thing2 has an additional field")
    public String s;
}
