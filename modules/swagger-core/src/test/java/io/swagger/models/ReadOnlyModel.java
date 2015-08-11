package io.swagger.models;

import io.swagger.annotations.ApiModelProperty;

public class ReadOnlyModel {
  private Integer id;
  private Integer readWriteId;

  @ApiModelProperty(readOnly = true)
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  @ApiModelProperty(readOnly = false)
  public Integer getReadWriteId() {
    return readWriteId;
  }
  public void setReadWriteId(Integer readWriteId) {
    this.readWriteId = readWriteId;
  }
}