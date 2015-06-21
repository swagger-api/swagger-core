package io.swagger.models.auth;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import io.swagger.models.deserializers.auth.SecuritySchemeTypeResolver;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(SecuritySchemeTypeResolver.class)
public interface SecuritySchemeDefinition {
    String getType();

    void setType(String type);
}