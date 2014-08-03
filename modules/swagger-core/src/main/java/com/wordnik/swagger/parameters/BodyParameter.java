package com.wordnik.swagger.parameters;

import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "in", "description", "required", "type", "items", "collectionFormat"})
public class BodyParameter extends AbstractParameter implements Parameter {;
  Property schema;

  public BodyParameter() {
    super.setIn("body");
  }

  public BodyParameter schema(Property schema) {
    this.setSchema(schema);
    return this;
  }
  public BodyParameter description(String description) {
    this.setDescription(description);
    return this;
  }
  public BodyParameter name(String name) {
    this.setName(name);
    return this;
  }

  public void setSchema(Property schema) {
    this.schema = schema;
  }
  public Property getSchema() {
    return schema;
  }
}