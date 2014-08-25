package com.wordnik.swagger.models.properties;

public class DateTimeProperty extends AbstractProperty implements Property {
  public DateTimeProperty() {
    super.type = "string";
    super.format = "date-time";
  }

  public static boolean isType(String type, String format) {
    if("string".equals(type) && "date-time".equals(format))
      return true;
    else return false;
  }
}