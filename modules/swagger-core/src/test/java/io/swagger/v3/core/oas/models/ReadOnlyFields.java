package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class ReadOnlyFields {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    public Long id;
}
