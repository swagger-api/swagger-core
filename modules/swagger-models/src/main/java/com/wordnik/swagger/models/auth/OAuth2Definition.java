package com.wordnik.swagger.models.auth;

import java.util.*;

public class OAuth2Definition implements SecuritySchemeDefinition {
  private String type = "oauth2";
  private String authorizationUrl;
  private String tokenUrl;
  private String flow;
  private Map<String, String> scopes;

  public OAuth2Definition () { }

  public OAuth2Definition implicit(String authorizationUrl) {
    this.setAuthorizationUrl(authorizationUrl);
    this.setFlow("implicit");
    return this;
  }
  public OAuth2Definition password(String tokenUrl) {
    this.setTokenUrl(tokenUrl);
    this.setFlow("password");
    return this;
  }
  public OAuth2Definition application(String tokenUrl) {
    this.setTokenUrl(tokenUrl);
    this.setFlow("application");
    return this;
  }
  public OAuth2Definition accessCode(String authorizationUrl, String tokenUrl) {
    this.setTokenUrl(tokenUrl);
    this.setAuthorizationUrl(authorizationUrl);
    this.setFlow("accessCode");
    return this;
  }
  public OAuth2Definition scope(String name, String description) {
    this.addScope(name, description);
    return this;
  }

  public String getAuthorizationUrl() {
    return authorizationUrl;
  }
  public void setAuthorizationUrl(String authorizationUrl) {
    this.authorizationUrl = authorizationUrl;
  }

  public String getTokenUrl() {
    return tokenUrl;
  }
  public void setTokenUrl(String tokenUrl) {
    this.tokenUrl = tokenUrl;
  }

  public String getFlow() {
    return flow;
  }
  public void setFlow(String flow) {
    this.flow = flow;
  }

  public Map<String, String> getScopes() {
    return scopes;
  }
  public void setScopes(Map<String, String> scopes) {
    this.scopes = scopes;
  }
  public void addScope(String name, String description) {
    if(this.scopes == null)
      this.scopes = new HashMap<String, String>();
    this.scopes.put(name, description);
  }

  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
}
