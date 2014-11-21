package com.wordnik.swagger.models.auth;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
  property = "type")
@JsonSubTypes({  
  @Type(value = BasicAuthDefinition.class, name = "basic"),
  @Type(value = ApiKeyAuthDefinition.class, name = "apiKey"),
  @Type(value = OAuth2Definition.class, name = "oauth2")
})

public interface SecuritySchemeDefinition {
  String getType();
  void setType(String type);
}