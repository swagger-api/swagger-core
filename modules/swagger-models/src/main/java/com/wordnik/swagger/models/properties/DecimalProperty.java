package com.wordnik.swagger.models.properties;

public class DecimalProperty extends AbstractNumericProperty implements Property {
  public DecimalProperty() {
    super.type = "number";
  }

  public static boolean isType(String type, String format) {
    if("number".equals(type) && format == null)
      return true;
    else return false;
  }
}