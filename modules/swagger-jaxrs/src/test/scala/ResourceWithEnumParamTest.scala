import testresources._

import com.wordnik.swagger.jaxrs.reader._
import com.wordnik.swagger.model._
import com.wordnik.swagger.config._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ResourceWithEnumParamTest extends FlatSpec with Matchers {
  it should "read an api with an enum param and return class and reflect those in the swagger model" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[ResourceWithEnumParam], config).getOrElse(fail("should not be None"))

    apiResource.apis.size should be (1)
    apiResource.models should be (None)

    val api = apiResource.apis.filter(_.path == "/enum_param/{access}/").head
    val ops = api.operations

    ops.size should be (1)

    val op = ops.head

    op.responseClass should be ("string")
    op.method should be ("GET")

    op.parameters.size should be (1)
    val param = op.parameters.head

    param.dataType should be ("string")

    param.allowableValues should be (AllowableListValues(List("PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY")))
    param.defaultValue should be (Some("PUBLIC"))
  }
}
