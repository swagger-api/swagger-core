package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.core.jackson.PathsSerializer;
import io.swagger.v3.oas.models.Paths;

import java.util.Map;

public abstract class OpenAPI31Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonSerialize(using = PathsSerializer.class)
    public abstract Paths getPaths();

}
