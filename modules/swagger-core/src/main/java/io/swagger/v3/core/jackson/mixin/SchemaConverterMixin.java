package io.swagger.v3.core.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.models.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SchemaConverterMixin {

    @JsonIgnore
    public abstract Map<String, Object> getJsonSchema();

    @JsonAnyGetter
    public abstract Map<String, Object> getExtensions();

    @JsonAnySetter
    public abstract void addExtension(String name, Object value);

    @JsonIgnore
    public abstract boolean getExampleSetFlag();

    @JsonInclude(value = JsonInclude.Include.NON_NULL, content = JsonInclude.Include.ALWAYS)
    public abstract Object getExample();

    @JsonIgnore
    public abstract Object getJsonSchemaImpl();

    @JsonIgnore
    public abstract BigDecimal getExclusiveMinimumValue();

    @JsonIgnore
    public abstract BigDecimal getExclusiveMaximumValue();

    @JsonIgnore
    public abstract Schema getContains();

    @JsonIgnore
    public abstract String get$id();

    @JsonIgnore
    public abstract String get$anchor();

    @JsonIgnore
    public abstract String get$schema();

    @JsonIgnore
    public abstract Set<String> getTypes();

    @JsonIgnore
    public abstract Map<String, Schema> getPatternProperties();

    @JsonIgnore
    public abstract List<Schema> getPrefixItems();

    @JsonIgnore
    public abstract String getContentEncoding();

    @JsonIgnore
    public abstract String getContentMediaType();

    @JsonIgnore
    public abstract Schema getContentSchema();

    @JsonIgnore
    public abstract Schema getPropertyNames();

    @JsonIgnore
    public abstract Object getUnevaluatedProperties();

    @JsonIgnore
    public abstract Integer getMaxContains();

    @JsonIgnore
    public abstract Integer getMinContains();

    @JsonIgnore
    public abstract Schema getAdditionalItems();

    @JsonIgnore
    public abstract Schema getUnevaluatedItems();

    @JsonIgnore
    public abstract Schema getIf();

    @JsonIgnore
    public abstract Schema getElse();

    @JsonIgnore
    public abstract Schema getThen();

    @JsonIgnore
    public abstract Map<String, Schema> getDependentSchemas();

    @JsonIgnore
    public abstract Map<String, List<String>> getDependentRequired();

    @JsonIgnore
    public abstract String get$comment();

    @JsonIgnore
    public abstract List<Object> getExamples();

    @JsonIgnore
    public abstract Object getConst();

}
