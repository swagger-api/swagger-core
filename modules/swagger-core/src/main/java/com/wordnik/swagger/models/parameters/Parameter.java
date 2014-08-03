package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.Property;

public interface Parameter {
  Property getType();
  void setType(Property type);

  String getDescription();
  void setDescription(String description);
}