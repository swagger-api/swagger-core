package com.wordnik.swagger.models;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.wordnik.swagger.models.properties.Property;

public class Response {
  private String description;
  private Property schema;
  private Map<String, Object> examples;
  private Map<String, Property> headers;

  public Response schema(Property property) {
    this.setSchema(property);
    return this;
  }
  public Response description(String description) {
    this.setDescription(description);
    return this;
  }
  public Response example(String type, Object example) {
    if(examples == null) {
      examples = new HashMap<String, Object>();
    }
    examples.put(type, example);
    return this;
  }
  public Response header(String name, Property property) {
    addHeader(name, property);
    return this;
  }
  public Response headers(Map<String, Property> headers) {
    this.headers = headers;
    return this;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public Property getSchema() {
    return schema;
  }
  public void setSchema(Property schema) {
    this.schema = schema;
  }

  public Map<String, Object> getExamples() {
    return this.examples;
  }
  public void setExamples(Map<String, Object> examples) {
    this.examples = examples;
  }

  public Map<String, Property> getHeaders() {
    return headers;
  }
  public void setHeaders(Map<String, Property> headers) {
    this.headers = headers;
  }
  public void addHeader(String key, Property property) {
    if(this.headers == null)
      this.headers = new LinkedHashMap<String, Property>();
    this.headers.put(key, property);
  }
}
