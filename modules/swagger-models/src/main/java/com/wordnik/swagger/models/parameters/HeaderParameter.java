package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "in", "description", "required", "type", "items", "collectionFormat"})
public class HeaderParameter extends AbstractParameter implements SerializableParameter {;
  String type;
  String format;
  String collectionFormat;
  Property items;

  public HeaderParameter() {
    super.setIn("header");
  }

  public HeaderParameter property(Property property) {
    this.setProperty(property);
    return this;
  }
  public HeaderParameter description(String description) {
    this.setDescription(description);
    return this;
  }
  public HeaderParameter name(String name) {
    this.setName(name);
    return this;
  }
  public HeaderParameter collectionFormat(String collectionFormat) {
    this.setCollectionFormat(collectionFormat);
    return this;
  }
  public HeaderParameter array(boolean isArray) {
    this.setArray(isArray);
    return this;
  }
  public HeaderParameter items(Property items) {
    this.items = items;
    return this;
  }

  public void setArray(boolean isArray) {
    this.type = "array";
  }

  public void setItems(Property items) {
    this.items = items;
  }
  public Property getItems() {
    return items;
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

  public String getCollectionFormat() {
    return collectionFormat;
  }
  public void setCollectionFormat(String collectionFormat) {
    this.collectionFormat = collectionFormat;
  }

  public void setProperty(Property property) {
    this.type = property.getType();
    this.format = property.getFormat();
  }
}