package com.wordnik.swagger.models.properties;

public class MapProperty extends AbstractProperty implements Property {
  Property property;

  public MapProperty() {
    super.type = "object";
  }
  public MapProperty additionalProperties(Property property) {
    this.setAdditionalProperties(property);
    return this;
  }

  public MapProperty(Property property) {
    this.property = property;
  }

  public Property getAdditionalProperties() {
    return property;
  }
  public void setAdditionalProperties(Property property) {
    this.property = property;
  }
}