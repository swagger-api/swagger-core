package com.wordnik.swagger.models.properties;

public class DateTimeProperty extends AbstractProperty implements Property {
  public DateTimeProperty() {
    super.type = "string";
    super.format = "date-time";
  }
}