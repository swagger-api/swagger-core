package com.wordnik.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "name", "in", "description", "required", "type", "items", "collectionFormat", "default"})
public class HeaderParameter extends AbstractParameter implements SerializableParameter {;
  protected String type;
  protected String format;
  protected String collectionFormat;
  protected Property items;

  @JsonProperty("default")
  protected String defaultValue;
  protected List<String> _enum;

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
  public HeaderParameter _enum(List<String> value) {
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
