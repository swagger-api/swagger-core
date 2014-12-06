package com.wordnik.swagger.models.parameters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
  property = "in")
@JsonSubTypes({  
  @Type(value = BodyParameter.class, name = "body"),
  @Type(value = HeaderParameter.class, name = "header"),
  @Type(value = PathParameter.class, name = "path"),
  @Type(value = QueryParameter.class, name = "query"),
  @Type(value = FormParameter.class, name = "formData"),
  @Type(value = CookieParameter.class, name = "cookie")})
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