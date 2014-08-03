package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "in", "description", "required", "type", "items", "collectionFormat"})
public class QueryParameter extends AbstractParameter implements Parameter {;
  Property type;

  public QueryParameter() {
    super.setIn("query");
  }

  public QueryParameter type(Property type) {
    this.setType(type);
    return this;
  }
  public QueryParameter description(String description) {
    this.setDescription(description);
    return this;
  }
  public QueryParameter name(String name) {
    this.setName(name);
    return this;
  }
  public QueryParameter required(boolean required) {
    this.setRequired(required);
    return this;
  }

  public void setType(Property type) {
    this.type = type;
  }
  public Property getType() {
    return type;
  }
}