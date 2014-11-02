package com.wordnik.swagger.models.parameters;

import com.fasterxml.jackson.annotation.*;

public abstract class AbstractParameter {
  @JsonIgnore
  protected String in;
  protected String name;
  protected String description;
  protected boolean required = false;

  public String getIn() {
    return in;
  }
  public void setIn(String in) {
    this.in = in;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public boolean getRequired() {
    return required;
  }
  public void setRequired(boolean required) {
    this.required = required;
  }
}
