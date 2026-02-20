package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

public abstract class ExampleMixin {
    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.ALWAYS)
    public abstract Object getValue();

    @JsonIgnore
    public abstract boolean getValueSetFlag();
}
