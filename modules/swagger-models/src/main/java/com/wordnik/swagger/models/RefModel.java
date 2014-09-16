package com.wordnik.swagger.models;

import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class RefModel implements Model {
  String ref;
  private String description;
  private Map<String, Property> properties;
  private String example;

  public RefModel(){}
  public RefModel(String ref){
    set$ref(ref);
  }

  public RefModel asDefault(String ref) {
    this.set$ref("#/definitions/" + ref);
    return this;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public Map<String, Property> getProperties() {
    return properties;
  }
  public void setProperties(Map<String, Property> properties) {
    this.properties = properties;
  }

  @JsonIgnore
  public String getSimpleRef() {
    if(ref.indexOf("#/definitions/") == 0)
      return ref.substring("#/definitions/".length());
    else
      return ref;
  }

  public String get$ref() {
    return ref;
  }
  public void set$ref(String ref) {
    this.ref = ref;
  }

  public String getExample() {
    return example;
  }
  public void setExample(String example) {
    this.example = example;
  }
}