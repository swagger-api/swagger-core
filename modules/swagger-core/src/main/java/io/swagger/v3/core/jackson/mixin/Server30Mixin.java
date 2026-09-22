package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * Mixin applied to {@link io.swagger.v3.oas.models.servers.Server} for the OpenAPI 3.0
 * and 3.1 mappers: {@code name} is a fixed field of the Server Object only as of
 * OpenAPI 3.2, so it is hidden (and ignored on deserialization) for earlier versions.
 */
public abstract class Server30Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonIgnore
    public abstract String getName();
}
