package io.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.models.Xml;

import java.util.Map;

public interface Property {
    Property title(String title);

    Property description(String description);

    String getType();

    String getFormat();

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String title);

    @JsonIgnore
    String getName();

    void setName(String name);

    @JsonIgnore
    boolean getRequired();

    void setRequired(boolean required);

    String getExample();

    void setExample(String example);

    Boolean getReadOnly();

    void setReadOnly(Boolean readOnly);

    Integer getPosition();

    void setPosition(Integer position);

    Xml getXml();

    void setXml(Xml xml);

    void setDefault(String _default);

    @JsonIgnore
    String getAccess();

    @JsonIgnore
    void setAccess(String access);

    Map<String, Object> getVendorExtensions();
}
