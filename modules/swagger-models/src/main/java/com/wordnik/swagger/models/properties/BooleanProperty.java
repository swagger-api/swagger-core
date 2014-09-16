package com.wordnik.swagger.models.properties;

public class BooleanProperty extends AbstractProperty implements Property {
  public BooleanProperty() {
    super.type = "boolean";
  }

  public BooleanProperty example(Boolean example) {
    this.setExample(String.valueOf(example));
    return this;
  }

  public static boolean isType(String type, String format) {
    if("boolean".equals(type))
      return true;
    else return false;
  }
}