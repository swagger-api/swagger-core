package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * Mixin applied to {@link io.swagger.v3.oas.models.security.SecurityScheme} for the
 * OpenAPI 3.0 and 3.1 mappers: {@code deprecated} and {@code oauth2MetadataUrl}
 * are fixed fields of the Security Scheme Object only as of OpenAPI 3.2.
 */
public abstract class SecurityScheme30Mixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonIgnore
    public abstract Boolean getDeprecated();

    @JsonIgnore
    public abstract String getOauth2MetadataUrl();
}
