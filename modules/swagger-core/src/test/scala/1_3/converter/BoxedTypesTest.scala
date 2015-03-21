package converter

import com.wordnik.swagger.converter._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.util.Json

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.collection.JavaConverters._
import scala.annotation.meta.field

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class BoxedTypesTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  it should "format a BoxedType" in {
    val models = ModelConverters.getInstance().read(classOf[BoxedTypesIssue31])
    models.size should be (1)

    val model = models.get("BoxedTypesIssue31")
    val properties = model.getProperties()
    properties should not be (null)
    properties.size should be (5)

models should serializeToJson (
"""{
  "BoxedTypesIssue31" : {
    "properties" : {
      "stringSeq" : {
        "type" : "array",
        "items" : {
          "type" : "string"
        }
      },
      "stringOpt" : {
        "type" : "array",
        "items" : {
          "type" : "string"
        }
      },
      "intSeq" : {
        "type" : "array",
        "description" : "Integers in a Sequence Box",
        "items" : {
          "type" : "object"
        }
      },
      "intOpt" : {
        "type" : "array",
        "description" : "Integer in an Option Box",
        "items" : {
          "type" : "object"
        }
      },
      "justInt" : {
        "type" : "integer",
        "format" : "int32"
      }
    },
    "description" : "Options of boxed types produces an Object ref instead of correct type"
  }
}""")
  }

  it should "format a BoxedTypeWithDataType provided in the annotation for the boxed object types" in {
    val models = ModelConverters.getInstance().read(classOf[BoxedTypesIssue31WithDataType])

    models.size should be (1)

    val model = models.get("BoxedTypesIssue31WithDataType")
    val properties = model.getProperties()
    properties should not be (null)
    properties.size should be (5)

    models should serializeToJson (
"""{
  "BoxedTypesIssue31WithDataType" : {
    "properties" : {
      "stringSeq" : {
        "type" : "array",
        "items" : {
          "type" : "string"
        }
      },
      "stringOpt" : {
        "type" : "array",
        "items" : {
          "type" : "string"
        }
      },
      "intSeq" : {
        "type" : "array",
        "description" : "Integers in a Sequence Box",
        "items" : {
          "type" : "integer",
          "format" : "int32"
        }
      },
      "intOpt" : {
        "type" : "integer",
        "format" : "int32",
        "description" : "Integer in an Option Box"
      },
      "justInt" : {
        "type" : "integer",
        "format" : "int32"
      }
    },
    "description" : "Options of boxed types produces an Object ref instead of correct type, but can be overcome with dataType"
  }
}""")
    // model.properties.size should be(5)
    // write(model) should be( """{"id":"BoxedTypesIssue31WithDataType","description":"Options of boxed types produces an Object ref instead of correct type, but can be overcome with dataType","properties":{"stringSeq":{"type":"array","items":{"type":"string"}},"stringOpt":{"type":"string"},"intSeq":{"type":"array","description":"Integers in a Sequence Box","items":{"type":"integer","format":"int32"}},"intOpt":{"type":"integer","format":"int32","description":"Integer in an Option Box"},"justInt":{"type":"integer","format":"int32"}}}""")
  }
}

@ApiModel(description = "Options of boxed types produces an Object ref instead of correct type")
case class BoxedTypesIssue31(
  stringSeq: Seq[String],
  stringOpt: Option[String],
  @(ApiModelProperty @field)(value = "Integers in a Sequence Box") intSeq: Seq[Int],
  @(ApiModelProperty @field)(value = "Integer in an Option Box") intOpt: Option[Int],
  justInt: Int)

// Get around the erasure by providing the dataType explicitly using the dataType common names.
@ApiModel(description = "Options of boxed types produces an Object ref instead of correct type, but can be overcome with dataType")
case class BoxedTypesIssue31WithDataType(
  stringSeq: Seq[String], stringOpt: Option[String],
  @(ApiModelProperty @field)(value = "Integers in a Sequence Box", dataType = "List[int]") intSeq: Seq[Int],
  @(ApiModelProperty @field)(value = "Integer in an Option Box", dataType = "int") intOpt: Option[Int],
  justInt: Int)
