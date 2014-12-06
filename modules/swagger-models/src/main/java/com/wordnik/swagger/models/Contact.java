package com.wordnik.swagger.models;

public class Contact {
  private String name;
  private String url;
  private String email;

  public Contact name(String name) {
    this.setName(name);
    return this;
  }
  public Contact url(String url) {
    this.setUrl(url);
    return this;
  }
  public Contact email(String email) {
    this.setEmail(email);
    return this;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
}