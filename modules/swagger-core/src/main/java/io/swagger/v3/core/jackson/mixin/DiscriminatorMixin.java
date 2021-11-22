package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public abstract class DiscriminatorMixin {

    @JsonIgnore
    public abstract Map<String, Object> getExtensions();
}
