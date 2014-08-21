package com.wordnik.swagger.models.properties;

import java.util.Map;

public abstract class AbstractProperty {
  String name;
  String type;
  String format;
  Xml xml;
  boolean required;
  Integer position;

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }
  public Integer getPosition() {
    return position;
  }

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

  public boolean getRequired() {
    return required;
  }
  public void setRequired(boolean required) {
    this.required = required;
  }
}