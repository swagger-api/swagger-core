package models

import scala.annotation.meta.{getter, field}
import com.wordnik.swagger.annotations.{ApiModelProperty, ApiModel}


@ApiModel(description = "I am an Abstract Base Model with Sub-Types",
  discriminator = "_type",
  subTypes = Array(classOf[Thing1], classOf[Thing2]))
trait AbstractBaseModelWithSubTypes {
  @(ApiModelProperty @getter)(value = "This value is used as a discriminator for serialization") val _type: String
  @(ApiModelProperty @getter)(value = "An arbitrary field") val a: String
  @(ApiModelProperty @getter)(value = "An arbitrary field") val b: String
}

@ApiModel(description = "Shake hands with Thing1", parent = classOf[AbstractBaseModelWithSubTypes])
case class Thing1(
                   @(ApiModelProperty @field)(value = "Override the abstract a") a: String,
                   @(ApiModelProperty @field)(value = "Thing1 has an additional field") x: Int)
  extends AbstractBaseModelWithSubTypes {
  val _type = "Thing1"
  val b = "bThing"
}

@ApiModel(description = "and Thing2", parent = classOf[AbstractBaseModelWithSubTypes])
case class Thing2(
                   @(ApiModelProperty @field)(value = "Override the abstract a") a: String,
                   @(ApiModelProperty @field)(value = "Thing2 has an additional field") s: String)
  extends AbstractBaseModelWithSubTypes {
  val _type = "Thing2"
  val b = "bThing"
}

@ApiModel(description = "I am an Abstract Base Model without any declared fields and with Sub-Types",
  subTypes = Array(classOf[Thing3]))
trait AbstractBaseModelWithoutFields {
}

@ApiModel(description = "Thing3", parent = classOf[AbstractBaseModelWithoutFields])
case class Thing3(
                  @(ApiModelProperty @field)(value = "Additional field a") a: String,
                  @(ApiModelProperty @field)(value = "Additional field x") x: Int)
  extends AbstractBaseModelWithoutFields