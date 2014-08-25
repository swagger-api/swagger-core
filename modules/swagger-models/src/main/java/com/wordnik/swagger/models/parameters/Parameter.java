package com.wordnik.swagger.models.parameters;

import com.wordnik.swagger.models.properties.Property;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
  property = "in")
@JsonSubTypes({  
  @Type(value = BodyParameter.class, name = "body"),
  @Type(value = HeaderParameter.class, name = "header"),
  @Type(value = PathParameter.class, name = "path"),
  @Type(value = QueryParameter.class, name = "query"),
  @Type(value = CookieParameter.class, name = "cookie")})
public interface Parameter {
  String getIn();
  void setIn(String in);

  String getName();
  void setName(String name);

  String getDescription();
  void setDescription(String description);

  boolean getRequired();
}