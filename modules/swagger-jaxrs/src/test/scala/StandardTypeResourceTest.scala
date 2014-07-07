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
class StandardTypeResourceTest extends FlatSpec with Matchers {
  it should "read an api and extract an error model" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[ResourceWithStandardTypes], config).getOrElse(fail("should not be None"))
    apiResource.apis.size should be (3)

    val api1 = apiResource.apis.filter(_.path == "/standard/{id}/strings").head
    val ops = api1.operations
    ops.size should be (1)

    val getOp = ops.head
    getOp.method should be ("GET")
    getOp.parameters.size should be (1)
    val param = getOp.parameters.head
    param.name should be ("id")
    param.dataType should be ("List[int]")
    param.required should be (true)
  }
}