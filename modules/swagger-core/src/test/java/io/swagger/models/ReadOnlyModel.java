package io.swagger.models;

import io.swagger.annotations.media.OASSchema;

public class ReadOnlyModel {
  private Integer id;
  private Integer readWriteId;

  @OASSchema(readOnly = true)
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  @OASSchema(readOnly = false)
  public Integer getReadWriteId() {
    return readWriteId;
  }
  public void setReadWriteId(Integer readWriteId) {
    this.readWriteId = readWriteId;
  }
}