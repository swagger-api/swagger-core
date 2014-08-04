package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "in", "description", "required", "type", "items", "collectionFormat"})
public class CookieParameter extends AbstractParameter implements Parameter {;
  String type;
  String format;

  public CookieParameter() {
    super.setIn("cookie");
    super.setRequired(true);
  }

  public CookieParameter property(Property property) {
    this.setProperty(property);
    return this;
  }
  public CookieParameter description(String description) {
    this.setDescription(description);
    return this;
  }
  public CookieParameter name(String name) {
    this.setName(name);
    return this;
  }

  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  public String getFormat() {
    return format;
  }
  public void setFormat(String format) {
    this.format = format;
  }

  public void setProperty(Property property) {
    this.type = property.getType();
    this.format = property.getFormat();
  }
}