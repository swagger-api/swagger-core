package io.swagger.models.properties;

import io.swagger.models.Xml;

public class ObjectProperty extends AbstractProperty implements Property {
  public static final String TYPE = "object";

  public ObjectProperty() {
    super.type = TYPE;
  }
  public ObjectProperty xml(Xml xml) {
    this.setXml(xml);
    return this;
  }

  public ObjectProperty example(String example) {
    this.setExample(example);
    return this;
  }

  public static boolean isType(String type) {
    return TYPE.equals(type);
  }

  //TODO: implement additional formats
  public static boolean isType(String type, String format) {
    return isType(type);
  }
}
