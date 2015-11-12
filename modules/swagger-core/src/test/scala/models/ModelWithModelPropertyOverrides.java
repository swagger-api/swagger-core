package models;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.*;

public class ModelWithModelPropertyOverrides {
  @ApiModelProperty(dataType = "List[models.Children]")
  private String children;

  public String getChildren() {
    return children;
  }

  public void setChildren(String children) {
    this.children = children;
  }
}