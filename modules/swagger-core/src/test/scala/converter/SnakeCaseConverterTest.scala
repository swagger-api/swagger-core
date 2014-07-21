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

@RunWith(classOf[JUnitRunner])
class SnakeCaseConverterTest extends FlatSpec with Matchers {
  it should "ignore properties with type Bar" in {
    // add the custom converter
    val snakeCaseConverter = new SnakeCaseConverter
    ModelConverters.addConverter(snakeCaseConverter, true)

    // make sure the field bar: converter.Bar is not present
    ModelConverters.read(classOf[SnakeCaseModel]) match {
      case Some(model) => {
        model.id should be ("SnakeCaseModel")
        model.name should be ("SnakeCaseModel")
      }
      case _ => fail("didn't read anything")
    }
    
    // cleanup to avoid impacting other test cases with Date model members
    ModelConverters.removeConverter(snakeCaseConverter)
  }
}

@XmlRootElement(name="snakeCaseModel")
class SnakeCaseModel {
  @BeanProperty var bar:Bar = null
  @BeanProperty var title:String = null
}

class SnakeCaseConverter extends SwaggerSchemaConverter {
  override def read(cls: Class[_], typeMap: Map[String, String] = Map.empty): Option[Model] = {
    super.read(cls, typeMap).map(model => {
      val properties = new LinkedHashMap[String, ModelProperty]
 
      model.properties.keys.foreach(key => {
        val p = model.properties(key)
        val property = p.copy(`type` = toSnakeCase(p.`type`))
        properties += key -> property
      })
      model.copy(
        id = toSnakeCase(model.id), 
        name = toSnakeCase(model.name), 
        properties = properties)
    })
  }

  val primitives = Set("string", "integer", "number", "boolean", "long")
  def toSnakeCase(str: String) = {
    primitives.contains(str) match {
      case true => str
      case false => Character.toUpperCase(str.charAt(0)) + str.substring(1)
    }
  }
}
