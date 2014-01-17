package converter

import models._

import com.wordnik.swagger.converter._

import com.wordnik.swagger.core.util._

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.reflect.BeanProperty

@RunWith(classOf[JUnitRunner])
class JaveDateTimeOverride extends FlatSpec with ShouldMatchers {
  ModelConverters.addConverter(new JavaDateTimeConverter, true)
  val models = ModelConverters.readAll(classOf[ModelWithDate])
  JsonSerializer.asJson(models) should be ("""[{"id":"ModelWithDate","properties":{"dateValue":{"type":"integer","format":"int64"}}}]""")
}

class JavaDateTimeConverter extends SwaggerSchemaConverter {
  override def typeMap = Map("date" -> "long")
}

class ModelWithDate {
  @BeanProperty var dateValue: Date = _
}