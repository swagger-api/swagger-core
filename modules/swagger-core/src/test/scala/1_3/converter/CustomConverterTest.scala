package converter

import models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.util._
import com.wordnik.swagger.model._

import org.joda.time.DateTime

import java.lang.annotation.Annotation
import java.lang.reflect.Type

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
    val converters = new ModelConverters()
    converters.addConverter(new CustomConverter())

    val models = converters.read(classOf[Foo])

    val model = models.get("Foo")
    model should not be (null)
    model.getProperties.size should be (1)

    val barProperty = model.getProperties.get("bar")
    barProperty should be (null)

    val titleProperty = model.getProperties.get("title")
    titleProperty should not be (null)
  }
}

class CustomConverter extends ModelConverter {
  @Override
  def resolveProperty(`type`: Type, context: ModelConverterContext, annotations: Array[Annotation], chain: java.util.Iterator[ModelConverter]): Property = {
    val jType = Json.mapper().constructType(`type`)
    if(jType != null) {
      var cls = jType.getRawClass
      if(cls.equals(classOf[Bar]))
        null
      else
        chain.next().resolveProperty(`type`, context, annotations, chain);
    }
    else
      null
  }
  
  @Override
  def resolve(`type`: Type, context: ModelConverterContext, chain: java.util.Iterator[ModelConverter]): Model = {
    return chain.next().resolve(`type`, context, chain)
  }
}

class Foo {
  @BeanProperty var bar: Bar = null
  @BeanProperty var title: String = null
}

class Bar {
  @BeanProperty var foo: String = null
}
