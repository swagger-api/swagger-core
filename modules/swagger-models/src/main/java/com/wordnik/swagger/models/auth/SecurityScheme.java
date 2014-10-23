package com.wordnik.swagger.models.auth;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
  property = "type")
@JsonSubTypes({  
  @Type(value = BasicAuth.class, name = "basic"),
  @Type(value = ApiKeyAuth.class, name = "apiKey"),
  @Type(value = OAuth2.class, name = "oauth2")
})

public interface SecurityScheme {
  String getType();
  void setType(String type);
}