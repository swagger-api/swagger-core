package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.core.jackson.ApiResponsesSerializer;
import io.swagger.v3.core.jackson.CallbackSerializer;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.responses.ApiResponses;

import java.util.Map;

public abstract class OperationMixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonSerialize(contentUsing = CallbackSerializer.class)
    public abstract Map<String, Callback> getCallbacks();

    @JsonSerialize(using = ApiResponsesSerializer.class)
    public abstract ApiResponses getResponses();

}
