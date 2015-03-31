package com.wordnik.swagger.models.parameters;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;

public abstract class AbstractParameter {
  protected String in;
  protected String name;
  protected String description;
  protected boolean required = false;
  protected String access;
  protected String pattern;
  private final Map<String, Object> vendorExtensions = new HashMap<String, Object>();

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

  public String getPattern() {
    return pattern;
  }
  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  @JsonAnyGetter
  public Map<String, Object> getVendorExtensions() {
    return vendorExtensions;
  }

  @JsonAnySetter
  public void setVendorExtension(String name, Object value) {
    if (name.startsWith("x-")) {
      vendorExtensions.put(name, value);
    }
  }
}
