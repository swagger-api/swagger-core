package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;

public class MapProperty extends AbstractProperty implements Property {
  Property property;

  public MapProperty() {
    super.type = "object";
  }
  public MapProperty(Property property) {
    super.type = "object";
    this.property = property;
  }
  public MapProperty xml(Xml xml) {
    this.setXml(xml);
    return this;
  }

  public MapProperty additionalProperties(Property property) {
    this.setAdditionalProperties(property);
    return this;
  }

  public MapProperty description(String description) {
    this.setDescription(description);
    return this;
  }

  public Property getAdditionalProperties() {
    return property;
  }
  public void setAdditionalProperties(Property property) {
    this.property = property;
  }

  public static boolean isType(String type, String format) {
    if("object".equals(type) && format == null)
      return true;
    return false;
  }
}