package models;

import models.composition.Pet;

import com.wordnik.swagger.annotations.*;

public class Car {
  @ApiModelProperty(readOnly = true)
  public Integer getWheelCount() {
    return new Integer(4);
  }
  public void setWheelCount(Integer wheelCount) {
    // does nothing
  }
}