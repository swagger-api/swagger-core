package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class ReadOnlyFields {
    @Schema(readOnly = true)
    public Long id;
}
