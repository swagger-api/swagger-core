package com.wordnik.swagger.models.properties;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Property {
  Property title(String title);
  Property description(String description);
  
  String getType();
  String getFormat();

  String getTitle();
  void setTitle(String title);

  String getDescription();
  void setDescription(String title);

  @JsonIgnore
  String getName();
  void setName(String name);

  @JsonIgnore
  boolean getRequired();

  String getExample();
  void setExample(String example);

  // @JsonIgnore
  void setPosition(Integer position);
  Integer getPosition();

  void setRequired(boolean required);
}