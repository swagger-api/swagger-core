package io.swagger.models;

import io.swagger.models.properties.Property;

import java.util.Map;

/**
 * Created by russellb337 on 7/9/15.
 */
public interface Response {
    ResponseImpl schema(Property property);

    ResponseImpl description(String description);

    ResponseImpl example(String type, Object example);

    ResponseImpl header(String name, Property property);

    ResponseImpl headers(Map<String, Property> headers);

    String getDescription();

    void setDescription(String description);

    Property getSchema();

    void setSchema(Property schema);

    Map<String, Object> getExamples();

    void setExamples(Map<String, Object> examples);

    Map<String, Property> getHeaders();

    void setHeaders(Map<String, Property> headers);

    void addHeader(String key, Property property);
}
