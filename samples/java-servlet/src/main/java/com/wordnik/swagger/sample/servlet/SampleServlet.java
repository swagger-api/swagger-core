package com.wordnik.swagger.sample.servlet;

import javax.servlet.http.HttpServlet;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.Authorization;
import com.wordnik.swagger.annotations.AuthorizationScope;

@Api(value = "/pet", description = "Operations about pets", authorizations = {
  @Authorization(value = "petstore_auth", type = "oauth2",
    scopes = {
      @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
      @AuthorizationScope(scope = "read:pets", description = "read your pets")
    })
}, tags = "pet")
public class SampleServlet extends HttpServlet {

  public void a() {

  }
}
