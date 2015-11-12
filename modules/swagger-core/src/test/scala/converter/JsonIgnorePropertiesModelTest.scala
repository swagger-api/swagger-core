package converter

import models._

import com.wordnik.swagger.converter._

import com.wordnik.swagger.core.util._

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class JsonIgnorePropertiesModelTest extends FlatSpec with Matchers {
  it should "ignore a property with ignore annotations" in {
    val models = ModelConverters.readAll(classOf[ModelWithIgnorePropertiesAnnotation])
    JsonSerializer.asJson(models) should be ("""[{"id":"ModelWithIgnorePropertiesAnnotation","properties":{"name":{"type":"string"},"favoriteAnimal":{"type":"string"}}}]""")
  }

  it should "ignore a property with superclass #767" in {
    val models = ModelConverters.readAll(classOf[Foo1])
    models(0).properties.contains("bazField") should be (false)
  }
}