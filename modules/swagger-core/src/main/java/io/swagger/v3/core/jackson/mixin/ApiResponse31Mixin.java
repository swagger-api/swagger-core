package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.core.jackson.Header31Serializer;
import io.swagger.v3.core.jackson.Link31Serializer;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.links.Link;

import java.util.Map;

public abstract class ApiResponse31Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonSerialize(contentUsing = Header31Serializer.class)
    public abstract Map<String, Header> getHeaders();

    @JsonSerialize(contentUsing = Link31Serializer.class)
    public abstract Map<String, Link> getLinks();
}
