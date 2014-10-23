package com.wordnik.swagger.models.auth;

public class ApiKeyAuth extends AbstractAuthScheme implements SecurityScheme {
  private String name;
  private In in;

  public ApiKeyAuth () {
    super.setType("apiKey");
  }

  public ApiKeyAuth(String name, In in) {
    super();
    this.setName(name);
    this.setIn(in);
  }

  public ApiKeyAuth name(String name) {
    this.setName(name);
    return this;
  }
  public ApiKeyAuth in(In in) {
    this.setIn(in);
    return this;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public In getIn() {
    return in;
  }
  public void setIn(In in) {
    this.in = in;
  }
}
