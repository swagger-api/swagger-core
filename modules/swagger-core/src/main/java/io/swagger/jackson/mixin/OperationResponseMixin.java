package io.swagger.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import io.swagger.models.Response;
import io.swagger.models.Responses;


import java.util.Map;

public abstract class OperationResponseMixin {

    @JsonIgnore
    public abstract Map<String,Response> getResponses();

    @JsonIgnore
    public abstract void setResponses(Map<String,Response> responses);

    @JsonGetter("responses")
    public abstract Responses getResponsesObject();

    @JsonSetter("responses")
    public abstract void getResponsesObject(Responses responsesObject);



}
