package com.wordnik.swagger.models.auth;

import com.fasterxml.jackson.annotation.*;

public abstract class AbstractAuthScheme {
  private String type;

  @JsonIgnore
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
}