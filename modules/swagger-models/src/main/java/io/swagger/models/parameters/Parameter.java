package io.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

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

    Boolean isReadOnly();

    void setReadOnly(Boolean readOnly);

    Boolean getAllowEmptyValue();

    void setAllowEmptyValue(Boolean allowEmptyValue);
}