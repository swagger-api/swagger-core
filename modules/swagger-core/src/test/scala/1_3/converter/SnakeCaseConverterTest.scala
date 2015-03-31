package converter

import models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models._

import java.lang.annotation.Annotation
import java.lang.reflect.Type
import java.util.LinkedHashMap
import javax.xml.bind.annotation._

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import matchers.SerializationMatchers._

@RunWith(classOf[JUnitRunner])
class SnakeCaseConverterTest extends FlatSpec with Matchers {
  it should "ignore properties with type Bar" in {
    // add the custom converter
    val snakeCaseConverter = new SnakeCaseConverter
    val converters = new ModelConverters()

    converters.addConverter(snakeCaseConverter)

    val models = converters.readAll(classOf[SnakeCaseModel])
    models should serializeToJson (
"""{
  "bar" : {
    "properties" : {
      "foo" : {
        "type" : "string"
      }
    }
  },
  "snake_case_model" : {
    "properties" : {
      "bar" : {
        "$ref" : "#/definitions/bar"
      },
      "title" : {
        "type" : "string"
      }
    },
    "xml" : {
      "name" : "snakeCaseModel"
    }
  }
}""")
  }
}

@XmlRootElement(name="snakeCaseModel")
class SnakeCaseModel {
  @BeanProperty var bar:Bar = null
  @BeanProperty var title:String = null
}

/**
 * simple converter to rename models and field names into snake_case
 */
class SnakeCaseConverter extends ModelConverter {
  def resolveProperty(`type`: Type, context: ModelConverterContext, annotations: Array[Annotation], chain: java.util.Iterator[ModelConverter]): Property = {
    if(chain.hasNext()) {
      val converter = chain.next()
      return converter.resolveProperty(`type`, context, annotations, chain)
    }
    return null
  }

  def resolve(`type`: Type, context: ModelConverterContext, chain: java.util.Iterator[ModelConverter]): Model = {
    if(chain.hasNext()) {
      val converter = chain.next()
      val model = converter.resolve(`type`, context, chain)
      if(model != null) {
        val properties = model.getProperties()
        val updatedProperties = new LinkedHashMap[String, Property]
        for(key <- properties.keySet.asScala) {
          val convertedKey = toSnakeCase(key)
          val prop = properties.get(key)
          if(prop.isInstanceOf[RefProperty]) {
            val ref = prop.asInstanceOf[RefProperty]
            ref.set$ref(toSnakeCase(ref.getSimpleRef()));
          }
          updatedProperties.put(convertedKey, prop)
        }
        model.getProperties().clear()
        model.setProperties(updatedProperties)
        val name = (model.asInstanceOf[ModelImpl]).getName
        if(model.isInstanceOf[ModelImpl]) {
          val impl = model.asInstanceOf[ModelImpl]
          impl.setName(toSnakeCase(impl.getName()))
        }
        return model
      }
    }
    return null
  }

  val primitives = Set("string", "integer", "number", "boolean", "long")
  def toSnakeCase(str: String) = {
    val o = "[A-Z\\d]".r.replaceAllIn(str, {m => "_" + m.group(0).toLowerCase()})
    if(o.startsWith("_")) o.substring(1)
    else o
  }
}
