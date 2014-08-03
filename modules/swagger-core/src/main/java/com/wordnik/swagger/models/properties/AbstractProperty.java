package com.wordnik.swagger.models.properties;

import java.util.Map;

public abstract class AbstractProperty {
  String type;
  String format;
  Xml xml;

  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  public String getFormat() {
    return format;
  }
  public void setFormat(String format) {
    this.format = format;
  }

  public Xml getXml() {
    return xml;
  }
  public void setXml(Xml xml) {
    this.xml = xml;
  }
}