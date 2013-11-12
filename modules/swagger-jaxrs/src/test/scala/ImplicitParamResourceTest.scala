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
class ImplicitParamResourceTest extends FlatSpec with ShouldMatchers {
  it should "read implicit params" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[ImplicitParamResource], config).getOrElse(fail("should not be None"))

    val apis = apiResource.apis
    apis.size should be (1)

    val ops = apis.head.operations
    ops.size should be (1)

    val op = ops.head
    op.parameters.size should be (1)
    val param = op.parameters(0)
    param.allowableValues should be (AllowableListValues(List("a","b","c")))
    param.name should be ("id")
    param.dataType should be ("int")
    param.allowMultiple should be (false)
    param.required should be (true)
  }
}
