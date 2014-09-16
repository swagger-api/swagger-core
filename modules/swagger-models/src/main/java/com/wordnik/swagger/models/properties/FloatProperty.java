package com.wordnik.swagger.models.properties;

public class FloatProperty extends AbstractNumericProperty implements Property {
  public FloatProperty() {
    super.type = "number";
    super.format = "float";
  }

  public FloatProperty example(Float example) {
    this.setExample(String.valueOf(example));
    return this;
  }

  public static boolean isType(String type, String format) {
    if("number".equals(type) && "float".equals(format))
      return true;
    else return false;
  }
}