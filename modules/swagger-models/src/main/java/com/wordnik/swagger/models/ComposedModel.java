package com.wordnik.swagger.models;

import com.wordnik.swagger.models.properties.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.*;

import javax.xml.bind.annotation.*;

public class ComposedModel implements Model {
  private List<Model> allOf = new ArrayList<Model>();
  private Model parent;
  private Model child;
  private String description;
  private String example;

  public ComposedModel parent(Model model) {
    this.setParent(model);
    return this;
  }
  public ComposedModel child(Model model) {
    this.setChild(model);
    return this;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public Map<String, Property> getProperties() {
    return null;
  }
  public void setProperties(Map<String, Property> properties){

  }

  public String getExample() {
    return example;
  }
  public void setExample(String example) {
    this.example = example;
  }

  public List<Model> getAllOf(){
    return allOf;
  }
  public void setAllOf(List<Model> allOf) {
    this.allOf = allOf;
  }

  @JsonIgnore
  public void setParent(Model model) {
    this.parent = model;
    this.allOf.add(model);
  }
  public Model getParent() {
    return parent;
  }

  @JsonIgnore
  public void setChild(Model model) {
    this.child = model;
    this.allOf.add(model);
  }
  public Model getChild() {
    return child;
  }
}