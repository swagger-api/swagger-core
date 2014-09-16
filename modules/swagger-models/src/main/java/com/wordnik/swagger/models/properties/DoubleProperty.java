package com.wordnik.swagger.models.properties;

public class DoubleProperty extends AbstractNumericProperty implements Property {
  public DoubleProperty() {
    super.type = "number";
    super.format = "double";
  }

  public DoubleProperty example(Double example) {
    this.setExample(String.valueOf(example));
    return this;
  }

  public static boolean isType(String type, String format) {
    if("number".equals(type) && "double".equals(format))
      return true;
    else return false;
  }
}