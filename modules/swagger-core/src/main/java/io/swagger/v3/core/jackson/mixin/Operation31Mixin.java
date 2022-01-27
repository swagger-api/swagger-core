package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.core.jackson.ApiResponses31Serializer;
import io.swagger.v3.core.jackson.Callback31Serializer;
import io.swagger.v3.core.jackson.Parameter31Serializer;
import io.swagger.v3.core.jackson.RequestBody31Serializer;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.List;
import java.util.Map;

public abstract class Operation31Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonSerialize(contentUsing = Callback31Serializer.class)
    public abstract Map<String, Callback> getCallbacks();

    @JsonSerialize(using = ApiResponses31Serializer.class)
    public abstract ApiResponses getResponses();

    @JsonSerialize(contentUsing = Parameter31Serializer.class)
    public abstract List<Parameter> getParameters();

    @JsonSerialize(using = RequestBody31Serializer.class)
    public abstract RequestBody getRequestBody();

}
