import com.wordnik.swagger.jersey.JerseyApiReader
import testresources._

import com.wordnik.swagger.jaxrs.reader._
import com.wordnik.swagger.model._
import com.wordnik.swagger.config._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class BeanParamTest extends FlatSpec with Matchers {
  it should "read beanparam parameters" in {
    val reader = new JerseyApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[BeanParamResource], config).getOrElse(fail("should not be None"))

    val apis = apiResource.apis
    apis.size should be (1)

    val ops = apis.head.operations
    ops.size should be (1)

    val op = ops.head
    op.parameters.size should be (3)

    val param0 = op.parameters(0)
    param0.name should be ("ids")
    param0.dataType should be ("Set[string]")
    param0.paramType should be ("query")

    val param1 = op.parameters(1)
    param1.name should be ("startDate")
    param1.dataType should be ("Date")
    param1.paramType should be ("query")

    val param2 = op.parameters(2)
    param2.name should be ("name")
    param2.dataType should be ("string")
    param2.paramType should be ("query")
  }
}
