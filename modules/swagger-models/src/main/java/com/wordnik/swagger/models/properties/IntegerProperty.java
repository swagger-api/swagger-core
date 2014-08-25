package com.wordnik.swagger.models.properties;

public class IntegerProperty extends AbstractProperty implements Property {
  public IntegerProperty() {
    super.type = "integer";
    super.format = "int32";
  }

  public static boolean isType(String type, String format) {
    if("integer".equals(type) && "int32".equals(format))
      return true;
    else return false;
  }
}