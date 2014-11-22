package com.wordnik.swagger.models.auth;

public class BasicAuthDefinition extends AbstractAuthScheme implements SecuritySchemeDefinition {
  public BasicAuthDefinition () {
    super.setType("basic");
  }
}
