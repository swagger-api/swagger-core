package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class RequiredFields {
    @Schema(description = "required", required = true)
    public Long required;

    @Schema(description = "not required")
    public Long notRequired;

    @Schema(description = "Optional field")
    public Optional<Long> optionalField;

    @Schema(description = "not required with annotation")
    @NotNull
    public Long notRequiredWithAnnotation;

    @Schema(description = "primitive type without constraint")
    public long primitiveTypeWithoutConstraint;

    @Schema(description = "primitive type with constraint")
    @NotNull
    public long primitiveTypeWithConstraint;

    @Schema(description = "mode auto", requiredMode = Schema.RequiredMode.AUTO)
    public Long modeAuto;

    @Schema(description = "mode auto with annotation", requiredMode = Schema.RequiredMode.AUTO)
    @NotNull
    public Long modeAutoWithAnnotation;

    @Schema(description = "mode required", requiredMode = Schema.RequiredMode.REQUIRED)
    public Long modeRequired;

    @Schema(description = "mode not required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Long modeNotRequired;

    @Schema(description = "mode not required with annotation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotNull
    public Long modeNotRequiredWithAnnotation;
}
