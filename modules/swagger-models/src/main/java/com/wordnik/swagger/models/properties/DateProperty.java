package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;

public class DateProperty extends AbstractProperty implements Property {
  public DateProperty() {
    super.type = "string";
    super.format = "date";
  }
  public DateProperty xml(Xml xml) {
    this.setXml(xml);
    return this;
  }  

  public DateProperty example(String example) {
    this.setExample(example);
    return this;
  }

  public static boolean isType(String type, String format) {
    if("string".equals(type) && "date".equals(format))
      return true;
    else return false;
  }
}