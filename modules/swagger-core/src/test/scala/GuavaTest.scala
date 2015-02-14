import models._
import models.composition.Pet;

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models.ModelImpl
import com.wordnik.swagger.models.properties._
import com.wordnik.swagger.converter._

import com.fasterxml.jackson.datatype.guava.GuavaModule

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class GuavaTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a model with Guava optionals" in {
    m.registerModule(new GuavaModule())

    val schemas = ModelConverters.getInstance().read(classOf[GuavaModel])
    m.writeValueAsString(schemas) should equal ("""{"GuavaModel":{"properties":{"name":{"type":"string"}}}}""")
  }
}