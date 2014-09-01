package com.wordnik.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RefProperty extends AbstractProperty implements Property {
  String ref;

  public RefProperty() {
    super.type = "ref";
  }

  public RefProperty(String ref) {
    super.type = "ref";
    set$ref(ref);
  }

  public RefProperty asDefault(String ref) {
    this.set$ref("#/definitions/" + ref);
    return this;
  }
  public RefProperty description(String description) {
    this.setDescription(description);
    return this;
  }

  @Override
  @JsonIgnore
  public String getType() {
    return this.type;
  }
  @Override
  @JsonIgnore
  public void setType(String type) {
    this.type = type;
  }

  public String get$ref() {
    return ref;
  }
  public void set$ref(String ref) {
    this.ref = ref;
  }

  public static boolean isType(String type, String format) {
    if("$ref".equals(type))
      return true;
    else return false;
  }
}