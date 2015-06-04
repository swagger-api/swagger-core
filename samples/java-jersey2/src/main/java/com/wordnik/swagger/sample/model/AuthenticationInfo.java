package io.swagger.sample.model;

import io.swagger.annotations.ApiParam;

import javax.ws.rs.*;

public class AuthenticationInfo {
  @HeaderParam("X-TOKEN")
  @ApiParam("The an expiring token assigned to a user")
  protected String token;
  @HeaderParam("X-REQUEST-HASH")
  @ApiParam("A HMAC-SHA hash based on the request being made")
  protected String hash;

  public String getHash() {
    return token;
  }
  public void setHash(String token) {
    this.token = token;
  }

  public String getToken() {
    return token;
  }
  public void setToken(String token) {
    this.token = token;
  }
}