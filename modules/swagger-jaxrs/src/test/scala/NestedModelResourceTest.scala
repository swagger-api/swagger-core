import testresources._

import com.wordnik.swagger.jaxrs.reader._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import com.wordnik.swagger.config._
import com.wordnik.swagger.core.filter._

import java.lang.reflect.Method

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class NestedModelResourceTest extends FlatSpec with ShouldMatchers {
  it should "read an api and extract an error model" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[UserResource], config).getOrElse(fail("should not be None"))

    apiResource.apis.size should be (1)

    val models = apiResource.models.get
    models.size should be (3)
    (models.keys.toSet & Set("Wheel", "Car", "User")).size should be (3)
  }

  it should "maintain models after filter" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[UserResource], config).getOrElse(fail("should not be None"))

    val filtered = new SpecFilter().filter(apiResource, FilterFactory.filter, Map(), Map(), Map())

    val models = filtered.models.get
    (models.keys.toSet & Set("Wheel", "Car", "User")).size should be (3)
  }
}