package models

import models.OrderSize.OrderSize

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }

import scala.annotation.meta.field

@ApiModel(description = "Scala model containing an Enumeration Value that is annotated with the dataType of the Enumeration class")
case class SModelWithEnum(
  @(ApiModelProperty @field)(value = "Textual label") label: Option[String] = None,
  @(ApiModelProperty @field)(value = "Order Size", dataType = "models.OrderSize$") orderSize: OrderSize = OrderSize.TALL)

@ApiModel(description = "Scala model containing an Enumeration Value that is not annotated with the dataType of the Enumeration class")
case class SModelWithEnumNoDataType(
  @(ApiModelProperty @field)(value = "Textual label") label: Option[String] = None,
  @(ApiModelProperty @field)(value = "Order Size") orderSize: OrderSize = OrderSize.TALL)

@ApiModel(description = "Scala model containing an Enumeration Value that is incorrectly annotated with a bad dataType of the Enumeration class")
case class SModelWithEnumBadDataType(
  @(ApiModelProperty @field)(value = "Textual label") label: Option[String] = None,
  @(ApiModelProperty @field)(value = "Order Size", dataType = "a.bad.dataType") orderSize: OrderSize = OrderSize.TALL)


case object OrderSize extends Enumeration(0) {
  type OrderSize = Value
  val TALL = Value("TALL")
  val GRANDE = Value("GRANDE")
  val VENTI = Value("VENTI")
}