package com.wordnik.swagger.models;

import com.wordnik.swagger.models.properties.Property;

import java.util.*;

public interface Model {
  String getDescription();
  void setDescription(String description);

  Map<String, Property> getProperties();
  void setProperties(Map<String, Property> properties);

  String getExample();
  void setExample(String example);

  ExternalDocs getExternalDocs();

  Object clone();
}
