package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.models.media.Encoding;

import java.util.List;
import java.util.Map;

/**
 * Mixin applied to {@link Encoding} for the OpenAPI 3.0 and 3.1 mappers:
 * {@code encoding}, {@code prefixEncoding} and {@code itemEncoding} are fixed
 * fields of the Encoding Object only as of OpenAPI 3.2.
 */
public abstract class Encoding30Mixin extends ExtensionsMixin {

    @JsonIgnore
    public abstract Map<String, Encoding> getEncoding();

    @JsonIgnore
    public abstract List<Encoding> getPrefixEncoding();

    @JsonIgnore
    public abstract Encoding getItemEncoding();
}
