package com.wordnik.swagger.models.properties;

import java.util.Map;

public class Xml {
  String namespace;
  String prefix;
  boolean attribute;
  boolean wrapped;

  public Xml(){}

  public Xml(String namespace, String prefix, boolean attribute, boolean wrapped) {
    this.namespace = namespace;
    this.prefix = prefix;
    this.attribute = attribute;
    this.wrapped = wrapped;
  }

  public String getNamespace() {
    return namespace;
  }
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String getPrefix() {
    return prefix;
  }
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public boolean getAttribute() {
    return attribute;
  }
  public void setAttribute(boolean attribute) {
    this.attribute = attribute;
  }

  public boolean getWrapped() {
    return wrapped;
  }
  public void setWrapped(boolean wrapped) {
    this.wrapped = wrapped;
  }
}