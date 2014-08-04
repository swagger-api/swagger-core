package com.wordnik.swagger.models;

public class RefModel extends Model {
  String ref;
  public RefModel(){}
  public RefModel asDefault(String ref) {
    this.set$ref("#/definitions/" + ref);
    return this;
  }

  public String get$ref() {
    return ref;
  }
  public void set$ref(String ref) {
    this.ref = ref;
  }
}