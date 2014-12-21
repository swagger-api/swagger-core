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
import org.scalatest.Matchers

import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class ResourceWithInvalidImplicitsTest extends FlatSpec with Matchers {
  it should "skip params with invalid implicit info" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[ResourceWithInvalidImplicits], config).getOrElse(fail("should not be None"))

    val api = apiResource.apis.head
    val op = api.operations.head
    op.parameters.size should be (1)
    val param = op.parameters.head

    param.description.get should be ("An implicit param that is OK")
  }
}
