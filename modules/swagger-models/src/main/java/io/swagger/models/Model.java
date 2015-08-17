package io.swagger.models;

import io.swagger.models.properties.Property;

import java.util.Map;
import java.util.List;

public interface Model {
    String getDescription();

    void setDescription(String description);

    Map<String, Property> getProperties();

    void setProperties(Map<String, Property> properties);

    Object getExample();

    void setExample(Object example);

    ExternalDocs getExternalDocs();

    String getReference();

    void setReference(String reference);

    Object clone();

    Map<String, Object> getVendorExtensions();

    List<String> getEnum();

    void setEnum(List<String> _enum);
}
