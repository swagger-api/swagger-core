package com.wordnik.swagger.models.properties;

public class IntegerProperty extends AbstractProperty implements Property {
  public IntegerProperty() {
    super.type = "integer";
    super.format = "int32";
  }
}