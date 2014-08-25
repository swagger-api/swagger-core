package com.wordnik.swagger.models.properties;

public class LongProperty extends AbstractProperty implements Property {
  public LongProperty() {
    super.type = "integer";
    super.format = "int64";
  }

  public static boolean isType(String type, String format) {
    if("integer".equals(type) && "int64".equals(format))
      return true;
    else return false;
  }
}