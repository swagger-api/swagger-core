package models;

import com.fasterxml.jackson.annotation.*;

public class JacksonReadonlyModel {
  @JsonIgnore
  public void setCount(Integer count) {

  }

  @JsonProperty
  public Integer getCount() {
    return null;
  }
}