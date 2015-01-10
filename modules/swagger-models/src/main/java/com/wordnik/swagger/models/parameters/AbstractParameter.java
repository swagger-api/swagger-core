package com.wordnik.swagger.models.parameters;


public abstract class AbstractParameter {
  protected String in;
  protected String name;
  protected String description;
  protected boolean required = false;
  protected String access;

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

  public String getAccess() {
    return access;
  }
  public void setAccess(String access) {
    this.access = access;
  }

}
