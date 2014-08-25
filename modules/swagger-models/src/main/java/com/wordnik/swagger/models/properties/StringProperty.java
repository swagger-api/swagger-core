package com.wordnik.swagger.models.properties;

public class StringProperty extends AbstractProperty implements Property {
  public StringProperty() {
    super.type = "string";
  }

  public static boolean isType(String type, String format) {
    if("string".equals(type) && format == null)
      return true;
    else return false;
  }
}