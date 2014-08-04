package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.Property;

public interface Parameter {
  void setName(String name);

  String getDescription();
  void setDescription(String description);
}