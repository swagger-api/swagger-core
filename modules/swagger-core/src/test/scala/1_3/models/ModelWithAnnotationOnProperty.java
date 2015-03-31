package models;

import com.wordnik.swagger.annotations.*;

@ApiModel(description = "my annotated model")
public class ModelWithAnnotationOnProperty {
  @ApiModelProperty(value = "the count of items", position = 1)
  private Integer count;

  public void setCount(Integer count) {
    this.count = count;
  }

  public Integer getCount() {
    return count;
  }
}
