package com.wordnik.swagger.models;

import java.util.*;

public class SecurityScope {
  private String name;
  private String description;

  public SecurityScope() {}
  public SecurityScope(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public SecurityScope name(String name) {
    this.setName(name);
    return this;
  }
  public SecurityScope description(String description) {
    this.setDescription(description);
    return this;
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
}
