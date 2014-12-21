package models

import com.wordnik.swagger.annotations.{ ApiModel, ApiModelProperty }

import scala.annotation.meta.field

// Issue #31: https://github.com/gettyimages/spray-swagger/issues/31
// It would be nice if the Seq[Int] and Option[Int] could create the proper spec, but due
// to erasure the parameterized types are only identified as Object
@ApiModel(description = "Options of boxed types produces an Object ref instead of correct type")
case class BoxedTypesIssue31(stringSeq: Seq[String], stringOpt: Option[String],
                             @(ApiModelProperty @field)(value = "Integers in a Sequence Box") intSeq: Seq[Int],
                             @(ApiModelProperty @field)(value = "Integer in an Option Box") intOpt: Option[Int],
                             justInt: Int)

// Get around the erasure by providing the dataType explicitly using the dataType common names.
@ApiModel(description = "Options of boxed types produces an Object ref instead of correct type, but can be overcome with dataType")
case class BoxedTypesIssue31WithDataType(stringSeq: Seq[String], stringOpt: Option[String],
                                         @(ApiModelProperty @field)(value = "Integers in a Sequence Box", dataType = "List[int]") intSeq: Seq[Int],
                                         @(ApiModelProperty @field)(value = "Integer in an Option Box", dataType = "int") intOpt: Option[Int],
                                         justInt: Int)
