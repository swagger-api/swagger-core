package io.swagger.models.auth;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.Map;

public interface SecuritySchemeDefinition {
    String getType();

    void setType(String type);

    @JsonAnyGetter
    Map<String, Object> getVendorExtensions();

    @JsonAnySetter
    void setVendorExtension(String name, Object value);

    String getDescription();

    void setDescription(String description);
}