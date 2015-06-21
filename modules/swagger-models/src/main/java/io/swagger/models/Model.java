package io.swagger.models;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import io.swagger.models.deserializers.model.ModelTypeResolver;
import io.swagger.models.properties.Property;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(ModelTypeResolver.class)
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
}
