package converter

import models._

import com.wordnik.swagger.converter._

import com.wordnik.swagger.core.util._

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.beans.BeanProperty

@RunWith(classOf[JUnitRunner])
class JaveDateTimeOverride extends FlatSpec with ShouldMatchers {
  val javaDateTimeConverter = new JavaDateTimeConverter
  ModelConverters.addConverter(javaDateTimeConverter, true)
  val models = ModelConverters.readAll(classOf[ModelWithDate])
  JsonSerializer.asJson(models) should be ("""[{"id":"ModelWithDate","properties":{"dateValue":{"type":"integer","format":"int64"}}}]""")
  // cleanup to avoid impacting other test cases with Date model members
  ModelConverters.removeConverter(javaDateTimeConverter)
}

class JavaDateTimeConverter extends SwaggerSchemaConverter {
  override def typeMap = Map("date" -> "long")
}

class ModelWithDate {
  @BeanProperty var dateValue: Date = _
}