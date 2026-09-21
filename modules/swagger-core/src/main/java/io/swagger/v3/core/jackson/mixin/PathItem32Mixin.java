package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.swagger.v3.oas.models.Operation;

import java.util.Map;

/**
 * Mixin applied to {@link io.swagger.v3.oas.models.PathItem} for the OpenAPI 3.2 mapper.
 *
 * <p>Identical to {@link PathItemMixin} except that {@code query} is not ignored: the
 * {@code query} HTTP method is a fixed field of the Path Item Object as of OpenAPI 3.2.
 */
public abstract class PathItem32Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    public abstract Operation getQuery();
}
