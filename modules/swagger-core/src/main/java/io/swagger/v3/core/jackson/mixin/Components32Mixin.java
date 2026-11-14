package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.core.jackson.CallbackSerializer;
import io.swagger.v3.oas.models.callbacks.Callback;

import java.util.Map;

/**
 * Mixin applied to {@link io.swagger.v3.oas.models.Components} for the
 * OpenAPI 3.2 mapper: same shape as {@link Components31Mixin}, but
 * {@code mediaTypes} is a fixed Components field as of 3.2 so it is not
 * ignored here.
 */
public abstract class Components32Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonSerialize(contentUsing = CallbackSerializer.class)
    public abstract Map<String, Callback> getCallbacks();

}
