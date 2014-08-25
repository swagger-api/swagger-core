package com.wordnik.swagger.models.properties;

public class FloatProperty extends AbstractProperty implements Property {
  public FloatProperty() {
    super.type = "number";
    super.format = "float";
  }

  public static boolean isType(String type, String format) {
    if("number".equals(type) && "float".equals(format))
      return true;
    else return false;
  }
}