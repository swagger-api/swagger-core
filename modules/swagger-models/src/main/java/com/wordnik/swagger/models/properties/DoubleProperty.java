package com.wordnik.swagger.models.properties;

public class DoubleProperty extends AbstractProperty implements Property {
  public DoubleProperty() {
    super.type = "number";
    super.format = "double";
  }
}