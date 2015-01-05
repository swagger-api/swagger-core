package com.wordnik.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

public interface Parameter {
  // @JsonIgnore
  String getIn();
  // @JsonIgnore
  void setIn(String in);

  @JsonIgnore
  String getAccess();
  @JsonIgnore
  void setAccess(String access);

  String getName();
  void setName(String name);

  String getDescription();
  void setDescription(String description);

  boolean getRequired();
  void setRequired(boolean required);
}