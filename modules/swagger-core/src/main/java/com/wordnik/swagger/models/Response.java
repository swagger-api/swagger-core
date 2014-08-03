package com.wordnik.swagger.models;

import com.wordnik.swagger.models.Model;
import com.wordnik.swagger.models.properties.Property;

public class Response {
  String description;
  Property schema;

  public Response schema(Property property) {
    this.setSchema(property);
    return this;
  }
  public Response description(String description) {
    this.setDescription(description);
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
}