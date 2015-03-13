package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;

public abstract class AbstractProperty implements Property {
  String name;
  String type;
  String format;
  String example;
  String _default;
  Xml xml;
  boolean required;
  Integer position;
  String description;
  String title;
  Boolean readOnly;

  public Property title(String title) {
    this.setTitle(title);
    return this;
  }
  public Property description(String description) {
    this.setDescription(description);
    return this;
  }
  public Property _default(String _default) {
    this.setDefault(_default);
    return this;
  }
  public Property readOnly() {
    this.setReadOnly(Boolean.TRUE);
    return this;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getExample() {
    return example;
  }
  public void setExample(String example) {
    this.example = example;
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

  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public String getDefault() {
    return _default;
  }
  public void setDefault(String _default) {
    this._default = _default;
  }

  public Boolean getReadOnly() {
    return readOnly;
  }
  public void setReadOnly(Boolean readOnly) {
    if(Boolean.FALSE.equals(readOnly))
      this.readOnly = null;
    else
      this.readOnly = readOnly;
  }
}