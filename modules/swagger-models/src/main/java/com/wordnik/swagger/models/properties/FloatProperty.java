package com.wordnik.swagger.models.properties;

public class FloatProperty extends AbstractProperty implements Property {
  public FloatProperty() {
    super.type = "number";
    super.format = "float";
  }
}