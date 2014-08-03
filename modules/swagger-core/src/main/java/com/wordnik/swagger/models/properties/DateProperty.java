package com.wordnik.swagger.models.properties;

public class DateProperty extends AbstractProperty implements Property {
  public DateProperty() {
    super.type = "string";
    super.format = "date";
  }
}