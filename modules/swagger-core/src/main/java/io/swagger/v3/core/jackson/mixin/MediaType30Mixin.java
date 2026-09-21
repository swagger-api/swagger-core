package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.models.media.Schema;

/**
 * Mixin applied to {@link io.swagger.v3.oas.models.media.MediaType} for the
 * OpenAPI 3.0 and 3.1 mappers: {@code itemSchema} is a fixed field of the Media
 * Type Object only as of OpenAPI 3.2.
 */
public abstract class MediaType30Mixin extends MediaTypeMixin {

    @JsonIgnore
    public abstract Schema getItemSchema();
}
