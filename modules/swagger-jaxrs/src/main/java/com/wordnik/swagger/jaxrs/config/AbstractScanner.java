package com.wordnik.swagger.jaxrs.config;

public abstract class AbstractScanner {
  private boolean prettyPrint = false;

  public boolean prettyPrint() {
    return prettyPrint;
  }

  public void setPrettyPrint(boolean shouldPrettyPrint) {
    this.prettyPrint = shouldPrettyPrint;
  }
}