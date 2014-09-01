package com.wordnik.swagger.models.properties;

public class DoubleProperty extends AbstractNumericProperty implements Property {
  public DoubleProperty() {
    super.type = "number";
    super.format = "double";
  }

  public static boolean isType(String type, String format) {
    if("number".equals(type) && "double".equals(format))
      return true;
    else return false;
  }
}