package io.swagger.models;

import io.swagger.models.properties.Property;

import java.util.Map;

public interface Model {
    String getTitle();

    void setTitle(String title);

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
}
