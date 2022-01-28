package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.core.jackson.ApiResponse31Serializer;
import io.swagger.v3.core.jackson.Callback31Serializer;
import io.swagger.v3.core.jackson.Header31Serializer;
import io.swagger.v3.core.jackson.Link31Serializer;
import io.swagger.v3.core.jackson.Parameter31Serializer;
import io.swagger.v3.core.jackson.PathItem31Serializer;
import io.swagger.v3.core.jackson.RequestBody31Serializer;
import io.swagger.v3.core.jackson.SecurityScheme31Serializer;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;

import java.util.Map;

public abstract class Components31Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonSerialize(contentUsing = Callback31Serializer.class)
    public abstract Map<String, Callback> getCallbacks();

    @JsonSerialize(contentUsing = ApiResponse31Serializer.class)
    public abstract Map<String, ApiResponse> getResponses();

    @JsonSerialize(contentUsing = Parameter31Serializer.class)
    public abstract Map<String, Parameter> getParameters();

    @JsonSerialize(contentUsing = RequestBody31Serializer.class)
    public abstract Map<String, RequestBody> getRequestBodies();

    @JsonSerialize(contentUsing = SecurityScheme31Serializer.class)
    public abstract Map<String, SecurityScheme> getSecuritySchemes();

    @JsonSerialize(contentUsing = Header31Serializer.class)
    public abstract Map<String, Header> getHeaders();

    @JsonSerialize(contentUsing = PathItem31Serializer.class)
    public abstract Map<String, PathItem> getPathItems();

    @JsonSerialize(contentUsing = Link31Serializer.class)
    public abstract Map<String, Link> getLinks();
}
