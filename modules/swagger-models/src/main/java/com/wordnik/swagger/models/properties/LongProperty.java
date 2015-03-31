package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;

public class LongProperty extends AbstractNumericProperty implements Property {
  public LongProperty() {
    super.type = "integer";
    super.format = "int64";
  }
  public LongProperty xml(Xml xml) {
    this.setXml(xml);
    return this;
  }

  public LongProperty example (Long example) {
    this.setExample(String.valueOf(example));
    return this;
  }

  public static boolean isType(String type, String format) {
    if("integer".equals(type) && "int64".equals(format))
      return true;
    else return false;
  }
}