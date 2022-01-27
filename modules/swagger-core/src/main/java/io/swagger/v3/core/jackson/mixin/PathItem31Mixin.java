package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.core.jackson.ApiResponses31Serializer;
import io.swagger.v3.core.jackson.Callback31Serializer;
import io.swagger.v3.core.jackson.Parameter31Serializer;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.List;
import java.util.Map;

public abstract class PathItem31Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonSerialize(contentUsing = Parameter31Serializer.class)
    public abstract List<Parameter> getParameters();

}
