package com.wordnik.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "name", "in", "description", "required", "type", "items", "collectionFormat", "default"})
public class CookieParameter extends AbstractParameter implements SerializableParameter {
  protected String type;
  protected String format;
  protected String collectionFormat;
  protected Property items;

  @JsonProperty("default")
  protected String defaultValue;
  protected List<String> _enum;

  public CookieParameter() {
    super.setIn("cookie");
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
  public CookieParameter collectionFormat(String collectionFormat) {
    this.setCollectionFormat(collectionFormat);
    return this;
  }
  public CookieParameter array(boolean isArray) {
    this.setArray(isArray);
    return this;
  }
  public CookieParameter items(Property items) {
    this.items = items;
    return this;
  }
  public CookieParameter _enum(List<String> value) {
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
