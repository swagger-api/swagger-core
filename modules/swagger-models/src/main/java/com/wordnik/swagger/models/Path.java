package com.wordnik.swagger.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.wordnik.swagger.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({ "get", "post", "put", "delete", "options", "patch"})
public class Path {
  private Operation get;
  private Operation put;
  private Operation post;
  private Operation delete;
  private Operation patch;
  private Operation options;
  private List<Parameter> parameters;
  private final Map<String, Object> vendorExtensions = new HashMap<String, Object>();

  public Path set(String method, Operation op) {
    if("get".equals(method))
      return get(op);
    if("put".equals(method))
      return put(op);
    if("post".equals(method))
      return post(op);
    if("delete".equals(method))
      return delete(op);
    if("patch".equals(method))
      return patch(op);
    if("options".equals(method))
      return options(op);
    return null;
  }

  public Path get(Operation get) {
    this.get = get;
    return this;
  }
  public Path put(Operation put) {
    this.put = put;
    return this;
  }
  public Path post(Operation post) {
    this.post = post;
    return this;
  }
  public Path delete(Operation delete) {
    this.delete = delete;
    return this;
  }
  public Path patch(Operation patch) {
    this.patch = patch;
    return this;
  }
  public Path options(Operation options) {
    this.options = options;
    return this;
  }

  public Operation getGet() {
    return get;
  }
  public void setGet(Operation get) {
    this.get = get;
  }

  public Operation getPut() {
    return put;
  }
  public void setPut(Operation put) {
    this.put = put;
  }

  public Operation getPost() {
    return post;
  }
  public void setPost(Operation post) {
    this.post = post;
  }

  public Operation getDelete() {
    return delete;
  }
  public void setDelete(Operation delete) {
    this.delete = delete;
  }

  public Operation getPatch() {
    return patch;
  }
  public void setPatch(Operation patch) {
    this.patch = patch;
  }

  public Operation getOptions() {
    return options;
  }
  public void setOptions(Operation options) {
    this.options = options;
  }

  @JsonIgnore
  public List<Operation> getOperations() {
    List<Operation> allOperations = new ArrayList<Operation>();
    if(get != null)
      allOperations.add(get);
    if(put != null)
      allOperations.add(put);
    if(post != null)
      allOperations.add(post);
    if(delete != null)
      allOperations.add(delete);
    if(patch != null)
      allOperations.add(patch);
    if(options != null)
      allOperations.add(options);

    return allOperations;
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

  @JsonIgnore
  public boolean isEmpty() {
    if(get == null && put == null && post == null && delete == null && patch == null && options == null)
      return true;
    else
      return false;
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
