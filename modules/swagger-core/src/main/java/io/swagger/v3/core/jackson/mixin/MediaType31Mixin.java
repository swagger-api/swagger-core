package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.core.jackson.Example31Serializer;
import io.swagger.v3.oas.models.examples.Example;

import java.util.Map;

public abstract class MediaType31Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonIgnore
    public abstract boolean getExampleSetFlag();

    @JsonInclude(JsonInclude.Include.CUSTOM)
    public abstract Object getExample();

    @JsonSerialize(contentUsing = Example31Serializer.class)
    public abstract Map<String, Example> getExamples();
}
