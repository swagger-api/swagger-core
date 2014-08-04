package com.wordnik.swagger.models;

public class Path {
  Operation get;
  Operation put;
  Operation post;
  Operation delete;
  Operation patch;
  Operation options;

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

}