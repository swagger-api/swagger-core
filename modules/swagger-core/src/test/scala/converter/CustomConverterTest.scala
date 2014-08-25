package converter

import model._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.model._

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._

import org.joda.time.DateTime

import scala.collection.mutable.LinkedHashMap
import scala.annotation.meta.field
import scala.beans.BeanProperty

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class CustomConverterTest extends FlatSpec with Matchers {
  it should "ignore properties with type Bar" in {
    // add the custom converter
    val customConverter = new CustomConverter
    ModelConverters.addConverter(customConverter, true)

    // make sure the field bar: converter.Bar is not present
    ModelConverters.read(classOf[Foo]) match {
      case Some(model) => model.properties.get("bar") should be (None)
      case _ => fail("didn't read anything")
    }
    ModelConverters.removeConverter(customConverter)
  }
}

class Foo {
  @BeanProperty var bar:Bar = null
  @BeanProperty var title:String = null
}

class Bar {
  @BeanProperty var foo:String = null
}

class CustomConverter extends SwaggerSchemaConverter {
  override def skippedClasses: Set[String] = Set("converter.Bar")
}