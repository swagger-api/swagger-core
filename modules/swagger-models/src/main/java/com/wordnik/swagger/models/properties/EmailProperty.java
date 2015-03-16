package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;

import java.util.*;

public class EmailProperty extends StringProperty {
  public EmailProperty() {
    super.type = "string";
    super.format = "email";
  }
  public EmailProperty(StringProperty prop) {
    this();
    this._enum = prop.getEnum();
    this.minLength = prop.getMinLength();
    this.maxLength = prop.getMaxLength();
    this.pattern = prop.getPattern();
    this.name = prop.getName();
    this.type = prop.getType();
    this.example = prop.getExample();
    this._default = prop.getDefault();
    this.xml = prop.getXml();
    this.required = prop.getRequired();
    this.position = prop.getPosition();
    this.description = prop.getDescription();
    this.title = prop.getTitle();
    this.readOnly = prop.getReadOnly();
  }

  public EmailProperty xml(Xml xml) {
    super.xml(xml);
    return this;
  }
  public EmailProperty example(String example) {
    super.example(example);
    return this;
  }
  public EmailProperty minLength(Integer minLength) {
    super.minLength(minLength);
    return this;
  }
  public EmailProperty maxLength(Integer maxLength) {
    super.maxLength(maxLength);
    return this;
  }
  public EmailProperty pattern(String pattern) {
    super.pattern(pattern);
    return this;
  }
  public EmailProperty _enum(String value) {
    super._enum(value);
    return this;
  }
  public EmailProperty _enum(List<String> value) {
    super._enum(value);
    return this;
  }

  public static boolean isType(String type, String format) {
    if("string".equals(type) && "email".equals(format))
      return true;
    else return false;
  }
}