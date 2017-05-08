package io.swagger.oas.models;

import io.swagger.annotations.media.OASSchema;

public class ModelWithOffset {
    public String id;

    @OASSchema(type = "java.time.OffsetDateTime")
    public String offset;
}
