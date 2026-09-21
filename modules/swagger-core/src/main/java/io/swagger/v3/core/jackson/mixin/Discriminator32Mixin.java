package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.Map;

/**
 * Mixin applied to {@link io.swagger.v3.oas.models.media.Discriminator} for the
 * OpenAPI 3.2 mapper. Identical to {@link Discriminator31Mixin} except that
 * {@code defaultMapping} is not ignored: it is a fixed field of the Discriminator
 * Object as of OpenAPI 3.2.
 */
public abstract class Discriminator32Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);
}
