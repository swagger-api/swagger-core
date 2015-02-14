package com.wordnik.swagger.models.auth;

public class AuthorizationValue {
  private String value, type, keyName;

  public AuthorizationValue(){}
  public AuthorizationValue(String keyName, String value, String type) {
    this.setKeyName(keyName);
    this.setValue(value);
    this.setType(type);
  }

  public AuthorizationValue value(String value) {
    this.value = value;
    return this;
  }
  public AuthorizationValue type(String type) {
    this.type = type;
    return this;
  }
  public AuthorizationValue keyName(String keyName) {
    this.keyName = keyName;
    return this;
  }

  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }

  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  public String getKeyName() {
    return keyName;
  }
  public void setKeyName(String keyName) {
    this.keyName = keyName;
  }
}