package models.composition;

import com.wordnik.swagger.annotations.*;

public interface Pet {
  @ApiModelProperty(position = 1, required = true, value = "The pet type")
  String getType();
  void setType(String type);

  @ApiModelProperty(position = 2, required = true, value = "The name of the pet")
  String getName();
  void setName(String name);

  @ApiModelProperty(position = 3, required = true)
  Boolean getIsDomestic();
  void setIsDomestic(Boolean isDomestic);
}