package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "in", "description", "required", "type", "items", "collectionFormat", "defaultValue"})
public class QueryParameter extends AbstractParameter implements SerializableParameter {;
  String type;
  String format;
  String collectionFormat;
  Property items;
  String defaultValue;

  public QueryParameter() {
    super.setIn("query");
  }

  public QueryParameter property(Property property) {
    this.setProperty(property);
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
  public QueryParameter collectionFormat(String collectionFormat) {
    this.setCollectionFormat(collectionFormat);
    return this;
  }
  public QueryParameter array(boolean isArray) {
    this.setArray(isArray);
    return this;
  }
  public QueryParameter items(Property items) {
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

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }
}
