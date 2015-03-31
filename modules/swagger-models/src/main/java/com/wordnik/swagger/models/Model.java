package com.wordnik.swagger.models;

import com.wordnik.swagger.models.properties.Property;

import java.util.*;

public interface Model {
  String getDescription();
  void setDescription(String description);

  Map<String, Property> getProperties();
  void setProperties(Map<String, Property> properties);

  Object getExample();
  void setExample(Object example);

  ExternalDocs getExternalDocs();

  Object clone();
}
