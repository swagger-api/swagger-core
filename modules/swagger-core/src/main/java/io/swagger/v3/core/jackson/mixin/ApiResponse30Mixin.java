package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * Mixin applied to {@link io.swagger.v3.oas.models.responses.ApiResponse} for the
 * OpenAPI 3.0 and 3.1 mappers: {@code summary} is a fixed field of the Response
 * Object only as of OpenAPI 3.2.
 */
public abstract class ApiResponse30Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonIgnore
    public abstract String getSummary();
}
