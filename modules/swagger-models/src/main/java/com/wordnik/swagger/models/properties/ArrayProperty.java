package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;

public class ArrayProperty extends AbstractProperty implements Property {
  public static final String TYPE = "array";
  protected Boolean uniqueItems;
  protected Property items;

  public ArrayProperty() {
    super.type = TYPE;
  }

  public ArrayProperty(Property items) {
    super.type = TYPE;
    setItems(items);
  }

  public ArrayProperty xml(Xml xml) {
    this.setXml(xml);
    return this;
  }

  public ArrayProperty uniqueItems() {
    this.setUniqueItems(true);
    return this;
  }

  public ArrayProperty description(String description) {
    this.setDescription(description);
    return this;
  }

  public ArrayProperty items(Property items) {
    setItems(items);
    return this;
  }

  public Property getItems() {
    return items;
  }

  public void setItems(Property items) {
    this.items = items;
  }

  public Boolean getUniqueItems() {
    return uniqueItems;
  }

  public void setUniqueItems(Boolean uniqueItems) {
    this.uniqueItems = uniqueItems ? true : null;
  }

  public static boolean isType(String type) {
    return TYPE.equals(type);
  }
}
