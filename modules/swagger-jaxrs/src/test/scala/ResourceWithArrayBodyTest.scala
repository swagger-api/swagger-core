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
class ResourceWithArrayBodyTest extends FlatSpec with ShouldMatchers {
  it should "read an api and extract an error model" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[ResourceWithArrayBody], config).getOrElse(fail("should not be None"))

    apiResource.apis.size should be (1)
    val api = apiResource.apis(0)
    api.operations.size should be (1)

    val op = api.operations(0)
    op.parameters.size should be (1)
    val param = op.parameters(0)
    param.dataType should be ("Array[int]")
  }
}