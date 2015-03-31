package com.wordnik.swagger.models.properties;

import com.wordnik.swagger.models.Xml;

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
    if(ref.startsWith("http"))
      return ref;
    else
      return "#/definitions/" + ref;
  }
  public void set$ref(String ref) {
    if(ref.indexOf("#/definitions/") == 0)
      this.ref = ref.substring("#/definitions/".length());
    else
      this.ref = ref;
  }

  @JsonIgnore
  public String getSimpleRef() {
    if(ref.indexOf("#/definitions/") == 0)
      return ref.substring("#/definitions/".length());
    else
      return ref;
  }

  public static boolean isType(String type, String format) {
    if("$ref".equals(type))
      return true;
    else return false;
  }
}