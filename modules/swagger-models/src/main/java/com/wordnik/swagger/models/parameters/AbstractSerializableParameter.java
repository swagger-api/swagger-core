package com.wordnik.swagger.models.parameters;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.wordnik.swagger.models.properties.ArrayProperty;
import com.wordnik.swagger.models.properties.Property;
import com.wordnik.swagger.models.properties.StringProperty;

@JsonPropertyOrder({"name", "in", "description", "required", "type", "items", "collectionFormat", "default", "maximum", "exclusiveMaximum", "minimum", "exclusiveMinimum"})
public abstract class AbstractSerializableParameter<T extends AbstractSerializableParameter<T>> extends AbstractParameter implements SerializableParameter {
  protected String type;
  protected String format;
  protected String collectionFormat;
  protected Property items;
  protected List<String> _enum;
  protected Boolean exclusiveMaximum;
  protected Double maximum;
  protected Boolean exclusiveMinimum;
  protected Double minimum;

  @JsonProperty("default")
  protected String defaultValue;

  public T property(Property property) {
    this.setProperty(property);
    return castThis();
  }

  public T type(String type) {
    this.setType(type);
    return castThis();
  }

  public T format(String format) {
    this.setFormat(format);
    return castThis();
  }

  public T description(String description) {
    this.setDescription(description);
    return castThis();
  }

  public T name(String name) {
    this.setName(name);
    return castThis();
  }

  public T required(boolean required) {
    this.setRequired(required);
    return castThis();
  }

  public T collectionFormat(String collectionFormat) {
    this.setCollectionFormat(collectionFormat);
    return castThis();
  }

  public T items(Property items) {
    this.items = items;
    return castThis();
  }

  public T _enum(List<String> value) {
    this._enum = value;
    return castThis();
  }

  public List<String> getEnum() {
    return _enum;
  }

  public void setEnum(List<String> _enum) {
    this._enum = _enum;
  }

  public Property getItems() {
    return items;
  }

  public void setItems(Property items) {
    this.items = items;
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
    if (property instanceof StringProperty) {
      final StringProperty string = (StringProperty) property;
      setEnum(string.getEnum());
    } else if (property instanceof ArrayProperty) {
      final ArrayProperty array = (ArrayProperty) property;
      setItems(array.getItems());
    }
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public Boolean isExclusiveMaximum() {
    return exclusiveMaximum;
  }

  public void setExclusiveMaximum(Boolean exclusiveMaximum) {
    this.exclusiveMaximum = exclusiveMaximum;
  }

  public Double getMaximum() {
    return maximum;
  }

  public void setMaximum(Double maximum) {
    this.maximum = maximum;
  }

  public Boolean isExclusiveMinimum() {
    return exclusiveMinimum;
  }

  public void setExclusiveMinimum(Boolean exclusiveMinimum) {
    this.exclusiveMinimum = exclusiveMinimum;
  }

  public Double getMinimum() {
    return minimum;
  }

  public void setMinimum(Double minimum) {
    this.minimum = minimum;
  }

  @JsonIgnore
  private T castThis() {
    @SuppressWarnings("unchecked")
    final T result = (T) this;
    return result;
  }
}
