package com.wordnik.swagger.parameters;

import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "in", "description", "required", "type", "items", "collectionFormat"})
public class PathParameter extends AbstractParameter implements Parameter {;
  Property type;

  public PathParameter() {
    super.setIn("path");
    super.setRequired(true);
  }

  public PathParameter type(Property type) {
    this.setType(type);
    return this;
  }
  public PathParameter description(String description) {
    this.setDescription(description);
    return this;
  }
  public PathParameter name(String name) {
    this.setName(name);
    return this;
  }

  public void setType(Property type) {
    this.type = type;
  }
  public Property getType() {
    return type;
  }
}