package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.AbstractProperty;
import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RefParameter extends AbstractParameter implements Parameter {
  String ref;

  public RefParameter(String ref) {
    set$ref(ref);
  }

  public RefParameter asDefault(String ref) {
    this.set$ref("#/parameters/" + ref);
    return this;
  }
  public RefParameter description(String description) {
    this.setDescription(description);
    return this;
  }

  public String get$ref() {
    if(ref.startsWith("http"))
      return ref;
    else
      return "#/parameters/" + ref;
  }
  public void set$ref(String ref) {
    if(ref.indexOf("#/parameters/") == 0)
      this.ref = ref.substring("#/parameters/".length());
    else
      this.ref = ref;
  }

  @Override
  @JsonIgnore
  public boolean getRequired() {
    return required;
  }

  @JsonIgnore
  public String getSimpleRef() {
    if(ref.indexOf("#/parameters/") == 0)
      return ref.substring("#/parameters/".length());
    else
      return ref;
  }

  public static boolean isType(String type, String format) {
    if("$ref".equals(type))
      return true;
    else return false;
  }
}