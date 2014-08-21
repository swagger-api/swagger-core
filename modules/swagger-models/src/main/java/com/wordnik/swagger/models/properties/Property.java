package com.wordnik.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Property {
  String getType();
  String getFormat();

  @JsonIgnore
  String getName();
  void setName(String name);

  @JsonIgnore
  boolean getRequired();

  // @JsonIgnore
  void setPosition(Integer position);
  Integer getPosition();
  void setRequired(boolean required);
}