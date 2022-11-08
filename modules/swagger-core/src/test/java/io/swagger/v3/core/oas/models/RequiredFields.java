package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class RequiredFields {
    @Schema(description = "required", required = true)
    public Long required;

    @Schema(description = "not required")
    public Long notRequired;

    @Schema(description = "not required with annotation")
    @NotNull
    public Long notRequiredWithAnnotation;

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
