package converter

import com.wordnik.swagger.core.SwaggerSpec
import com.wordnik.swagger.core.util.ModelUtil
import models._
import com.wordnik.swagger.model._
import com.wordnik.swagger.converter._
import org.json4s._
import org.json4s.jackson.Serialization.write
import org.json4s.jackson._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class BoxedTypesTest extends FlatSpec with Matchers {
  implicit val formats = SwaggerSerializers.formats

  "ModelConverters" should "format a BoxedType" in {
    val model = ModelConverters.read(classOf[BoxedTypesIssue31]).getOrElse(fail("no model found"))
    model.properties.size should be(5)
    write(model) should be( """{"id":"BoxedTypesIssue31","description":"Options of boxed types produces an Object ref instead of correct type","properties":{"stringSeq":{"type":"array","items":{"type":"string"}},"stringOpt":{"type":"string"},"intSeq":{"type":"array","description":"Integers in a Sequence Box","items":{"$ref":"Object"}},"intOpt":{"$ref":"Object","description":"Integer in an Option Box"},"justInt":{"type":"integer","format":"int32"}}}""")
  }

  it should "format a BoxedTypeWithDataType provided in the annotation for the boxed object types" in {
    val model = ModelConverters.read(classOf[BoxedTypesIssue31WithDataType]).getOrElse(fail("no model found"))
    model.properties.size should be(5)
    write(model) should be( """{"id":"BoxedTypesIssue31WithDataType","description":"Options of boxed types produces an Object ref instead of correct type, but can be overcome with dataType","properties":{"stringSeq":{"type":"array","items":{"type":"string"}},"stringOpt":{"type":"string"},"intSeq":{"type":"array","description":"Integers in a Sequence Box","items":{"type":"integer","format":"int32"}},"intOpt":{"type":"integer","format":"int32","description":"Integer in an Option Box"},"justInt":{"type":"integer","format":"int32"}}}""")
  }
}
