package com.wordnik.swagger.models.auth;

public class BasicAuthDefinition implements SecuritySchemeDefinition {
  private String type = "basic";

  public BasicAuthDefinition () { }

  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
}
