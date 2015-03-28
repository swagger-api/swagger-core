package com.wordnik.swagger.models;

import com.wordnik.swagger.models.properties.*;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class ComposedModel extends AbstractModel {
  private List<Model> allOf = new ArrayList<Model>();
  private Model parent;
  private Model child;
  private List<RefModel> interfaces;
  private String description;
  private Object example;

  public ComposedModel parent(Model model) {
    this.setParent(model);
    return this;
  }
  public ComposedModel child(Model model) {
    this.setChild(model);
    return this;
  }
  public ComposedModel interfaces(List<RefModel> interfaces) {
	    this.setInterfaces(interfaces);
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

  public Object getExample() {
    return example;
  }
  public void setExample(Object example) {
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
    if (!allOf.contains(model)) this.allOf.add(model);
  }
  public Model getParent() {
    return parent;
  }

  @JsonIgnore
  public void setChild(Model model) {
    this.child = model;
    if (!allOf.contains(model)) this.allOf.add(model);
  }
  public Model getChild() {
    return child;
  }
  
  @JsonIgnore
  public void setInterfaces(List<RefModel> interfaces) {
    this.interfaces = interfaces;
    for (RefModel model : interfaces) 
    	if (!allOf.contains(model)) allOf.add(model);
  }
  public List<RefModel> getInterfaces() {
    return interfaces;
  }
   
  
}