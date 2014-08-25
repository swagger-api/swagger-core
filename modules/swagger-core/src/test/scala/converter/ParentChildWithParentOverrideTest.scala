package converter

import model._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.model._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import scala.beans.BeanProperty
import scala.collection.mutable.LinkedHashMap
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import javax.xml.bind.annotation._
import converter.models.Parent
import converter.models.Child



@RunWith(classOf[JUnitRunner])
class ParentChildWithParentOverrideTest extends FlatSpec with Matchers {    
  it should "Json specified model should have the model of Child" in {
    val converter = new OverrideConverter()
    converter.add(classOf[Parent].getName(), """{"id":"Parent","properties":{"overriddenName":{"type":"string","qualifiedType":"java.lang.String"},"parentName":{"type":"string","qualifiedType":"java.lang.String"},"child":{"type":"Child","qualifiedType":"converter.models.Child"}}}""")
    ModelConverters.addConverter(converter, true)
    val a = ModelConverters.readAll(classOf[Parent])
    println(JsonSerializer.asJson(a))
    JsonSerializer.asJson(a) should be("""[{"id":"Parent","properties":{"overriddenName":{"type":"string"},"parentName":{"type":"string"},"child":{"$ref":"Child"}}},{"id":"Child","properties":{"childName":{"type":"string"}}}]""")
  }

  it should "API specified model should have the model of Child" in {

    val converter = new OverrideConverter()
    val props = new LinkedHashMap[String, ModelProperty]
    props += "overriddenName" -> ModelProperty(
      "string",
      "java.lang.String")
      
    props += "parentName" -> ModelProperty(
      "string",
      "java.lang.String")

    props += "child" -> ModelProperty(
      "Child",
      "converter.models.Child")

    var model = Model("Parent",
      "Parent",
      classOf[Parent].getName(),
      props)
    converter.add(classOf[Parent].getName(), model);
    ModelConverters.addConverter(converter, true)
    val a = ModelConverters.readAll(classOf[Parent])
       println(JsonSerializer.asJson(a))
    JsonSerializer.asJson(a) should be("""[{"id":"Parent","properties":{"overriddenName":{"type":"string"},"parentName":{"type":"string"},"child":{"$ref":"Child"}}},{"id":"Child","properties":{"childName":{"type":"string"}}}]""")
  }
}	