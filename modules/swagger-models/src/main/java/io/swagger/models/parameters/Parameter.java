package io.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import io.swagger.models.deserializers.parameter.ParameterTypeResolver;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(ParameterTypeResolver.class)
public interface Parameter {
    String getIn();

    void setIn(String in);

    @JsonIgnore
    String getAccess();

    @JsonIgnore
    void setAccess(String access);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    boolean getRequired();

    void setRequired(boolean required);

    String getPattern();

    void setPattern(String pattern);

    Map<String, Object> getVendorExtensions();
}