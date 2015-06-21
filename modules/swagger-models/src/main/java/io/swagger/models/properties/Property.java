package io.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import io.swagger.models.Xml;
import io.swagger.models.deserializers.property.PropertyTypeResolver;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(PropertyTypeResolver.class)
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

    void setReadOnly(Boolean example);

    Integer getPosition();

    void setPosition(Integer position);

    Xml getXml();

    void setXml(Xml xml);

    void setDefault(String _default);

    @JsonIgnore
    String getAccess();

    @JsonIgnore
    void setAccess(String access);
}
