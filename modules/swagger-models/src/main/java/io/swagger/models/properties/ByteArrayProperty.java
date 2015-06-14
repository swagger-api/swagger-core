package io.swagger.models.properties;

import io.swagger.models.Xml;

import java.util.*;

public class ByteArrayProperty extends AbstractProperty implements Property {


  public ByteArrayProperty() {
    super.type = "string";
    super.format = "byte";
  }

  public static boolean isType(String type, String format) {
    if("string".equals(type) &&  "byte".equals(format))
      return true;
    else return false;
  }
  public ByteArrayProperty xml(Xml xml) {
     this.setXml(xml);
     return this;
  }

  public ByteArrayProperty example(String example) {
     this.setExample(example);
     return this;
  }
}