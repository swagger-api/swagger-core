package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * Mixin applied to {@link io.swagger.v3.oas.models.tags.Tag} for the OpenAPI 3.0
 * and 3.1 mappers: {@code summary}, {@code parent} and {@code kind} are fixed
 * fields of the Tag Object only as of OpenAPI 3.2.
 */
public abstract class Tag30Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonIgnore
    public abstract String getSummary();

    @JsonIgnore
    public abstract String getParent();

    @JsonIgnore
    public abstract String getKind();
}
