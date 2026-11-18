package io.swagger.v3.core.resolving.resources;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;

public record JacksonUnwrappedRecord(
        @JsonUnwrapped @Schema(requiredMode = Schema.RequiredMode.REQUIRED) Inner inner,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED) Integer number) {

    public record Inner(@Schema(requiredMode = Schema.RequiredMode.REQUIRED) String value) {
    }
}
