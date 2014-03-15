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
class NonAnnotatedResourceTest extends FlatSpec with ShouldMatchers {
  it should "read a resource without Api annotations" in {
    val reader = new BasicJaxrsReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[NonAnnotatedResource], config).getOrElse(fail("should not be None"))

    apiResource.apis.map(p => println(p.path))

    apiResource.apis.size should be (2)
    val api = apiResource.apis.head
    api.path should be ("/basic/{id}")

    val ops = api.operations
    ops.size should be (2)
  }
}
