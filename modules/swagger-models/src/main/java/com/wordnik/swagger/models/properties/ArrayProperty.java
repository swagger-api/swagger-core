package com.wordnik.swagger.models.properties;

public class ArrayProperty extends AbstractProperty implements Property {
  Property items;
  public ArrayProperty() {
    super.type = "array";
  }
  public ArrayProperty(Property items) {
    super.type = "array";
    setItems(items);
  }

  public ArrayProperty description(String description) {
    this.setDescription(description);
    return this;
  }

  public ArrayProperty items(Property items) {
    setItems(items);
    return this;
  }

  public Property getItems() {
    return items;
  }
  public void setItems(Property items) {
    this.items = items;
  }
}