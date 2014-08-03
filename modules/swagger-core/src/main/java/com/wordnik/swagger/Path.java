package com.wordnik.swagger;

public class Path {
  Operation get;
  Operation put;
  Operation post;
  Operation delete;
  Operation patch;
  Operation options;

  public Path get(Operation get) {
    this.get = get;
    return this;
  }
  public Path post(Operation post) {
    this.post = post;
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