package com.wordnik.swagger.models.properties;

public class LongProperty extends AbstractProperty implements Property {
  public LongProperty() {
    super.type = "integer";
    super.format = "int64";
  }
}