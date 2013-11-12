package converter.models

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }

import scala.annotation.target.field

@ApiModel(value="a model with subtypes", discriminator = "name", subTypes = Array(classOf[DomesticAnimal], classOf[WildAnimal]))
case class Animal (
  @(ApiModelProperty @field)(value = "name of animal", position = 1) name: String,
  @(ApiModelProperty @field)(value = "date added", position = 2) date: java.util.Date)

case class DomesticAnimal (
  @(ApiModelProperty @field)(value = "name of animal", position = 1) name: String,
  @(ApiModelProperty @field)(value = "animals are safe for children", position = 2) safeForChildren: Boolean,
  @(ApiModelProperty @field)(value = "date added", position = 3) date: java.util.Date)

case class WildAnimal (
  @(ApiModelProperty @field)(value = "name of animal", position = 1) name: String,
  @(ApiModelProperty @field)(value = "location found in", position = 2) foundInLocation: String,
  @(ApiModelProperty @field)(value = "date added", position = 3) date: java.util.Date)