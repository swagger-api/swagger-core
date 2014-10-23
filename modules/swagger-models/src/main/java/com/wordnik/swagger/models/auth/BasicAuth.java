package com.wordnik.swagger.models.auth;

public class BasicAuth extends AbstractAuthScheme implements SecurityScheme {
  public BasicAuth () {
    super.setType("basic");
  }
}
