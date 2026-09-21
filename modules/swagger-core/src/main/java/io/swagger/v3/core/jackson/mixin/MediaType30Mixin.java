package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.Schema;

import java.util.List;

/**
 * Mixin applied to {@link io.swagger.v3.oas.models.media.MediaType} for the
 * OpenAPI 3.0 and 3.1 mappers: {@code itemSchema}, {@code prefixEncoding},
 * {@code itemEncoding} and {@code $ref} are fixed fields of the Media Type
 * Object (or allowed content map reference values) only as of OpenAPI 3.2.
 */
public abstract class MediaType30Mixin extends MediaTypeMixin {

    @JsonIgnore
    public abstract Schema getItemSchema();

    @JsonIgnore
    public abstract List<Encoding> getPrefixEncoding();

    @JsonIgnore
    public abstract Encoding getItemEncoding();

    @JsonIgnore
    public abstract String get$ref();
}
