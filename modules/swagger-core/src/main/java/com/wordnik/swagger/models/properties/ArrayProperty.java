package com.wordnik.swagger.models.properties;

public class ArrayProperty extends AbstractProperty implements Property {
  Property items;
  public ArrayProperty() {
    super.type = "array";
  }

  public Property getItems() {
    return items;
  }
  public void setItems(Property items) {
    this.items = items;
  }
}