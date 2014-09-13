package com.wordnik.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class SecurityDefinition {
  String name;
  String type;
  List<SecurityScope> scopes;

  public SecurityDefinition(){}
  public SecurityDefinition(String name, String type){
    this.name = name;
    this.type = type;
  }
  public SecurityDefinition scope(SecurityScope scope) {
    this.addScope(scope);
    return this;
  }

  @JsonIgnore
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  public List<SecurityScope> getScopes() {
    return scopes;
  }
  public void setScopes(List<SecurityScope> scopes) {
    this.scopes = scopes;
  }
  public void addScope(SecurityScope scope) {
    if(this.scopes == null)
      this.scopes = new ArrayList<SecurityScope>();
    this.scopes.add(scope);
  }
}