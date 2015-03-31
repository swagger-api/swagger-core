package com.wordnik.swagger.models;

import com.wordnik.swagger.models.parameters.*;

import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Operation {
  private List<String> tags;
  private String summary;
  private String description;
  private String operationId;
  private List<Scheme> schemes;
  private List<String> consumes;
  private List<String> produces;
  private List<Parameter> parameters = new ArrayList<Parameter>();
  private Map<String, Response> responses;
  private List<Map<String, List<String>>> security;
  private String example;
  private ExternalDocs externalDocs;
  private Boolean deprecated;
  private final Map<String, Object> vendorExtensions = new HashMap<String, Object>();

  public Operation summary(String summary) {
    this.setSummary(summary);
    return this;
  }
  public Operation description(String description) {
    this.setDescription(description);
    return this;
  }
  public Operation operationId(String operationId) {
    this.setOperationId(operationId);
    return this;
  }
  public Operation schemes(List<Scheme> schemes) {
    this.setSchemes(schemes);
    return this;
  }
  public Operation scheme(Scheme scheme) {
    this.addScheme(scheme);
    return this;
  }
  public Operation consumes(List<String> consumes) {
    this.setConsumes(consumes);
    return this;
  }
  public Operation consumes(String consumes) {
    this.addConsumes(consumes);
    return this;
  }
  public Operation produces(List<String> produces) {
    this.setProduces(produces);
    return this;
  }
  public Operation produces(String produces) {
    this.addProduces(produces);
    return this;
  }
  public Operation security(SecurityRequirement security) {
    this.addSecurity(security.getName(), security.getScopes());
    return this;
  }
  public Operation parameter(Parameter parameter) {
    this.addParameter(parameter);
    return this;
  }
  public Operation response(int key, Response response) {
    this.addResponse(String.valueOf(key), response);
    return this;
  }
  public Operation defaultResponse(Response response) {
    this.addResponse("default", response);
    return this;
  }
  public Operation tags(List<String> tags) {
    this.setTags(tags);
    return this;
  }
  public Operation tag(String tag) {
    this.addTag(tag);
    return this;
  }
  public Operation externalDocs(ExternalDocs externalDocs) {
    this.setExternalDocs(externalDocs);
    return this;
  }
  public Operation deprecated(Boolean deprecated) {
    this.setDeprecated(deprecated);
    return this;
  }

  public List<String> getTags() {
    return tags;
  }
  public void setTags(List<String> tags) {
    this.tags = tags;
  }
  public void addTag(String tag) {
    if(this.tags == null)
      this.tags = new ArrayList<String>();
    this.tags.add(tag);
  }

  public String getSummary() {
    return summary;
  }
  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public String getOperationId() {
    return operationId;
  }
  public void setOperationId(String operationId) {
    this.operationId = operationId;
  }

  public List<Scheme> getSchemes() {
    return schemes;
  }
  public void setSchemes(List<Scheme> schemes) {
    this.schemes = schemes;
  }
  public void addScheme(Scheme scheme) {
    if(this.schemes == null)
      this.schemes = new ArrayList<Scheme>();
    this.schemes.add(scheme);
  }

  public List<String> getConsumes() {
    return consumes;
  }
  public void setConsumes(List<String> consumes) {
    this.consumes = consumes;
  }
  public void addConsumes(String consumes) {
    if(this.consumes == null)
      this.consumes = new ArrayList<String>();
    this.consumes.add(consumes);
  }

  public List<String> getProduces() {
    return produces;
  }
  public void setProduces(List<String> produces) {
    this.produces = produces;
  }
  public void addProduces(String produces) {
    if(this.produces == null)
      this.produces = new ArrayList<String>();
    this.produces.add(produces);
  }

  public List<Parameter> getParameters() {
    return parameters;
  }
  public void setParameters(List<Parameter> parameters) {
    this.parameters = parameters;
  }
  public void addParameter(Parameter parameter) {
    if(this.parameters == null) {
      this.parameters = new ArrayList<Parameter>();
    }
    this.parameters.add(parameter);
  }

  public Map<String, Response> getResponses() {
    return responses;
  }
  public void setResponses(Map<String, Response> responses) {
    this.responses = responses;
  }
  public void addResponse(String key, Response response) {
    if(this.responses == null) {
      this.responses = new HashMap<String, Response>();
    }
    this.responses.put(key, response);
  }

  public List<Map<String, List<String>>> getSecurity() {
    return security;
  }
  public void setSecurity(List<Map<String, List<String>>> security) {
    this.security = security;
  }
  public void addSecurity(String name, List<String> scopes) {
    if(this.security == null)
      this.security = new ArrayList<Map<String, List<String>>>();
    Map<String, List<String>> req = new HashMap<String, List<String>>();
    if(scopes == null)
      scopes = new ArrayList<String>();
    req.put(name, scopes);
    this.security.add(req);
  }

  public ExternalDocs getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(ExternalDocs value) {
    this.externalDocs = value;
  }

  public Boolean isDeprecated() {
    return deprecated;
  }

  public void setDeprecated(Boolean value) {
    if(value == null || value.equals(Boolean.FALSE))
      this.deprecated = null;
    else
      this.deprecated = value;
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
}
