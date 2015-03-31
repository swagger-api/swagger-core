package models;

import com.fasterxml.jackson.annotation.*;
public class ModelWithJsonProperty {
  @JsonProperty("theCount")
  private Integer count;

  public void setCount(Integer count) {
    this.count = count;
  }

  public Integer getCount() {
    return count;
  }
}
