package io.swagger.oas.models;

import io.swagger.oas.annotations.media.Schema;

public class ReadOnlyFields {
    @Schema(readOnly = true)
    public Long id;
}
