package io.swagger.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

public class ReadOnlyModelUpdated {
  private Integer id;
  private Integer readWriteId;

  @JsonProperty
  private Integer autoId;

  @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_ONLY)
  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.READ_WRITE)
  public Integer getReadWriteId() {
    return readWriteId;
  }
  public void setReadWriteId(Integer readWriteId) {
    this.readWriteId = readWriteId;
  }

  @ApiModelProperty(accessMode = ApiModelProperty.AccessMode.AUTO)
  public Integer getAutoId() {
    return autoId;
  }
}