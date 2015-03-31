package com.wordnik.swagger.models;

import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;

import java.util.*;

public class Response {
  private String description;
  private Property schema;
  private Map<String, String> examples;
  private Map<String, Property> headers;

  public Response schema(Property property) {
    this.setSchema(property);
    return this;
  }
  public Response description(String description) {
    this.setDescription(description);
    return this;
  }
  public Response example(String type, String example) {
    if(examples == null) {
      examples = new HashMap<String, String>();
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

  public Map<String, String> getExamples() {
    return this.examples;
  }
  public void setExamples(Map<String, String> examples) {
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
