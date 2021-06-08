package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.models.media.Schema;

import java.math.BigDecimal;
import java.util.Map;

public abstract class SchemaMixin {

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonIgnore
    public abstract boolean getExampleSetFlag();

    @JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.ALWAYS)
    public abstract Object getExample();

    @JsonIgnore
    public abstract Map<String, Object> getJsonSchema();

    @JsonIgnore
    public abstract BigDecimal getExclusiveMinimumValue();

    @JsonIgnore
    public abstract BigDecimal getExclusiveMaximumValue();

    @JsonIgnore
    public abstract Map<String, Schema> getPatternProperties();

    @JsonIgnore
    public abstract Object getJsonSchemaImpl();
}
