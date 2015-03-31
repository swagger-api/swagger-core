package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;

public class IntegerProperty extends AbstractNumericProperty implements Property {
  public IntegerProperty() {
    super.type = "integer";
    super.format = "int32";
  }
  public IntegerProperty xml(Xml xml) {
    this.setXml(xml);
    return this;
  }

  public IntegerProperty example(Integer example) {
    this.setExample(String.valueOf(example));
    return this;
  }

  public static boolean isType(String type, String format) {
    if("integer".equals(type) && "int32".equals(format))
      return true;
    else return false;
  }
}