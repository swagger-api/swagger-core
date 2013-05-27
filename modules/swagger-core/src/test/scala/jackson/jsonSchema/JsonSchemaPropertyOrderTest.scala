package jackson.jsonSchema

import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._

import com.fasterxml.jackson.module.scala._

import com.fasterxml.jackson.annotation._
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsonSchema.factories.SchemaFactoryWrapper
import com.fasterxml.jackson.databind.jsonSchema.types._
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.core.JsonGenerator.Feature

import java.util.Date

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class JsonSchemaPropertyOrderTest extends FlatSpec with ShouldMatchers {
  // mapper setup
  val o = new ObjectMapper
  o.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

  val visitor = new SchemaFactoryWrapper
  o.registerModule(new DefaultScalaModule)

  it should "read a simple case class" in {
    val schema = schemaForClass(classOf[SimpleCaseClass]).asInstanceOf[ObjectSchema]
    val properties = schema.getProperties.asScala

    // check property order
    properties.map{_._1}.toList should equal (List("id", "name", "date"))

    // check property types
    properties.map{_._2.getType.toString}.toList should equal (List("NUMBER", "STRING", "STRING"))

    properties("date") match {
      case schema: StringSchema => schema.getFormat.toString should equal ("date-time")
      case _ => fail("unexpected type")
    }
  }

  /**
   * the order of properties is changing when processing the
   * inner object
   **/
  ignore should "read a nested case class" in {
    val schema = schemaForClass(classOf[NestedCaseClass]).asInstanceOf[ObjectSchema]
    schema.getProperties.asScala.map{_._1}.toList should equal (List("id", "firstName", "innerObject"))
  }

  /**
   * the order of properties is changing when processing the
   * Option value
   **/
  ignore should "read a case class with Options" in {
    val schema = schemaForClass(classOf[CaseClassWithOptions]).asInstanceOf[ObjectSchema]
    schema.getProperties.asScala.map{_._1}.toList should equal (List("id", "firstName", "age"))
  }

  /**
   * jackson seems to be flipping order of the properties
   * when @JsonProperty annotations are applied
   **/
  ignore should "read a case class with jackson annotations" in {
    val schema = schemaForClass(classOf[CaseClassWithAnnotations]).asInstanceOf[ObjectSchema]
    schema.getProperties.asScala.map{_._1}.toList should equal (List("identifier", "first", "age"))
  }

  /**
   * jackson seems to ignore required=true annotations when writing the json schema
   **/
  ignore should "honor required annotations" in {
    val schema = schemaForClass(classOf[CaseClassWithAnnotations]).asInstanceOf[ObjectSchema]
    schema.getProperties.asScala("identifier").getRequired should equal (true)
  }

  def schemaForClass(cls: Class[_]) = {
    o.acceptJsonFormatVisitor(o.constructType(cls), visitor)
    visitor.finalSchema
  }
}

case class SimpleCaseClass(
  id: Long,
  name: String,
  date: Date
)

case class NestedCaseClass(
  id: Long,
  firstName: String,
  innerObject: SimpleCaseClass
)

case class CaseClassWithOptions(
  id: Long,
  firstName: String,
  age: Option[Int]
)

case class CaseClassWithAnnotations(
  @JsonProperty(value="identifier", required=true) id: Long,
  @JsonProperty("first") firstName: String,
  @JsonProperty("age") @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00", timezone="CET") age: Option[Int]
)
