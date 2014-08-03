package com.wordnik.swagger;

public class Security {
  String name;

  public Security() {}
  public Security(String name) {
    this.name = name;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
}