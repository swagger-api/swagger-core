package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "name", "in", "description", "required", "type", "items", "collectionFormat", "default"})
public class PathParameter extends AbstractParameter implements SerializableParameter {;
  protected String type;
  protected String format;
  protected String collectionFormat;
  protected Property items;
  protected List<String> _enum;

  @JsonProperty("default")
  protected String defaultValue;

  public PathParameter() {
    super.setIn("path");
    super.setRequired(true);
  }

  public PathParameter array(boolean isArray) {
    this.setArray(isArray);
    return this;
  }
  public PathParameter collectionFormat(String collectionFormat) {
    this.setCollectionFormat(collectionFormat);
    return this;
  }
  public PathParameter description(String description) {
    this.setDescription(description);
    return this;
  }
  public PathParameter items(Property items) {
    this.setItems(items);
    return this;
  }
  public PathParameter name(String name) {
    this.setName(name);
    return this;
  }
  public PathParameter property(Property property) {
    this.setProperty(property);
    return this;
  }
  public PathParameter _enum(List<String> value) {
    this._enum = value;
    return this;
  }

  public List<String> getEnum() {
    return _enum;
  }
  public void setEnum(List<String> _enum) {
    this._enum = _enum;
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

  public String getFormat() {
    return format;
  }
  public void setFormat(String format) {
    this.format = format;
  }

  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
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
