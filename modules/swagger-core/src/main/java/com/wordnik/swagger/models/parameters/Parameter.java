package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.Property;

public interface Parameter {
  void setName(String name);
  String getIn();
  String getName();
  String getDescription();
  boolean getRequired();
  void setDescription(String description);
}