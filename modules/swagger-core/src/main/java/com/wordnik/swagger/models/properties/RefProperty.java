package com.wordnik.swagger.models.properties;

public class RefProperty extends AbstractProperty implements Property {
  String ref;

  public RefProperty() {}

  public RefProperty asDefault(String ref) {
    this.set$ref("#/definitions/" + ref);
    return this;
  }

  public RefProperty(String ref) {
    this.ref = ref;
  }

  public String get$ref() {
    return ref;
  }
  public void set$ref(String ref) {
    this.ref = ref;
  }
}