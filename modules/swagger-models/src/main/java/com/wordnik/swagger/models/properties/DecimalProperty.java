package com.wordnik.swagger.models.properties;

public class DecimalProperty extends AbstractNumericProperty implements Property {
  public DecimalProperty() {
    super.type = "number";
  }

  public DecimalProperty example(String example) {
    this.setExample(example);
    return this;
  }

  public static boolean isType(String type, String format) {
    if("number".equals(type) && format == null)
      return true;
    else return false;
  }
}