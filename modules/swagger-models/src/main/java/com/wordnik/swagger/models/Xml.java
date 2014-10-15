package com.wordnik.swagger.models;

public class Xml {
/*
  "name": { "type": "string"},
  "namespace": { "type": "string" },
  "prefix": { "type": "string" },
  "attribute": { "type": "boolean" },
  "wrapped": { "type": "boolean" }
*/
  private String name;
  private String namespace;
  private String prefix;
  private Boolean attribute;
  private Boolean wrapped;

  public Xml name(String name) {
    this.setName(name);
    return this;
  }
  public Xml namespace(String namespace) {
    this.setNamespace(namespace);
    return this;
  }
  public Xml prefix(String prefix) {
    this.setPrefix(prefix);
    return this;
  }
  public Xml attribute(Boolean attribute) {
    this.setAttribute(attribute);
    return this;
  }
  public Xml wrapped(Boolean wrapped) {
    this.setWrapped(wrapped);
    return this;
  }

  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getNamespace() {
    return this.namespace;
  }
  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public String getPrefix() {
    return this.prefix;
  }
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public Boolean getAttribute() {
    return this.attribute;
  }
  public void setAttribute(Boolean attribute) {
    this.attribute = attribute;
  }

  public Boolean getWrapped() {
    return this.wrapped;
  }
  public void setWrapped(Boolean wrapped) {
    this.wrapped = wrapped;
  }
}
