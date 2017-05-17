package io.swagger.oas.models;

import io.swagger.oas.annotations.media.Schema;

public class ModelWithOffset {
    public String id;

    @Schema(type = "java.time.OffsetDateTime")
    public String offset;
}
