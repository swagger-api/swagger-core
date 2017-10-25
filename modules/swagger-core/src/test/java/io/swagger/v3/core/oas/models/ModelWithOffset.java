package io.swagger.v3.core.oas.models;

import io.swagger.v3.oas.annotations.media.Schema;

public class ModelWithOffset {
    public String id;

    @Schema(implementation = java.time.OffsetDateTime.class)
    public String offset;
}
