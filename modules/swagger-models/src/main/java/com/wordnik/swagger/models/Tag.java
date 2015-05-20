package com.wordnik.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class Tag {
  private String name;
  private String description;
  private ExternalDocs externalDocs;
  private final Map<String, Object> vendorExtensions = new HashMap<String, Object>();

  public Tag name(String name) {
    setName(name);
    return this;
  }
  public Tag description(String description) {
    setDescription(description);
    return this;
  }
  public Tag externalDocs(ExternalDocs externalDocs) {
    setExternalDocs(externalDocs);
    return this;
  }

  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public ExternalDocs getExternalDocs() {
    return externalDocs;
  }
  public void setExternalDocs(ExternalDocs externalDocs) {
    this.externalDocs = externalDocs;
  }

  @JsonAnyGetter
  public Map<String, Object> getVendorExtensions() {
    return vendorExtensions;
  }

  @JsonAnySetter
  public void setVendorExtension(String name, Object value) {
    if (name.startsWith("x-")) {
      vendorExtensions.put(name, value);
    }
  }

  @Override
  public String toString() {
    StringBuilder b = new StringBuilder();
    b.append("Tag {\n");
    b.append("\tname: ").append(getName()).append("\n");
    b.append("\tdescription: ").append(getDescription()).append("\n");
    b.append("\texternalDocs: ").append(getExternalDocs()).append("\n");
    b.append("\textensions:" ).append(vendorExtensions.toString());
    b.append("}");
    return b.toString();
  }
}