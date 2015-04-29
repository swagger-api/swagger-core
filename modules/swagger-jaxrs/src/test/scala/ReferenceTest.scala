import com.wordnik.swagger.converter.ModelConverters
import models.Pet
import resources._

import com.wordnik.swagger.jaxrs.config._
import com.wordnik.swagger.models.parameters._
import com.wordnik.swagger.models.properties.{RefProperty, MapProperty}

import com.wordnik.swagger.models.Swagger
import com.wordnik.swagger.jaxrs.Reader
import com.wordnik.swagger.util.Json

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ReferenceTest extends FlatSpec with Matchers {

  it should "Scan a model with common reference and reference with ApiModel" in {
    val props = ModelConverters.getInstance().readAll(classOf[Pet]).get("Pet").getProperties()
    val category = props.get("category").asInstanceOf[RefProperty]
    category.getType() should be ("ref")
    category.get$ref() should be ("#/definitions/Category")

    val categoryWithApiModel = props.get("categoryWithApiModel").asInstanceOf[RefProperty]
    categoryWithApiModel.getType() should be ("ref")
    categoryWithApiModel.get$ref() should be ("#/definitions/MyCategory")
  }

}
