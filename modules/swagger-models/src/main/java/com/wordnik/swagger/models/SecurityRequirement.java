package com.wordnik.swagger.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.*;

public class SecurityRequirement {
  private String name;
  private String type;
  private List<String> scopes;

  public SecurityRequirement() {}
  public SecurityRequirement(String name) {
    this.name = name;
  }

  public SecurityRequirement scope(String scope) {
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

  public List<String> getScopes() {
    return scopes;
  }
  public void setScopes(List<String> scopes) {
    this.scopes = scopes;
  }
  public void addScope(String scope) {
    if(scopes == null)
      scopes = new ArrayList<String>();
    scopes.add(scope);
  }
}