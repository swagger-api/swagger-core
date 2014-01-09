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
class RootResourceTest extends FlatSpec with ShouldMatchers {
  it should "read a resource at the root of the servlet" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[RootResource], config).getOrElse(fail("should not be None"))
    println(JsonSerializer.asJson(apiResource))

    apiResource.apis.size should be (1)

    val api = apiResource.apis.filter(_.path == "/{id}").head
    val ops = api.operations
    ops.size should be (1)
  }
}