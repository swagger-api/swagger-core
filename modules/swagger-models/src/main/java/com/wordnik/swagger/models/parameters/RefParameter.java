package com.wordnik.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.models.properties.AbstractProperty;
import com.wordnik.swagger.models.properties.Property;

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
    return ref;
  }
  public void set$ref(String ref) {
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