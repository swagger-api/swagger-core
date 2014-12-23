import testresources._

import com.wordnik.swagger.jersey._
import com.wordnik.swagger.config._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ResourceWithDefaultValueParamTest extends FlatSpec with Matchers {
  it should "read an api with a parameter with a default value and reflect that in the swagger model" in {
    val reader = new JerseyApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[ResourceWithDefaultValueParam], config).getOrElse(fail("should not be None"))

    apiResource.apis.size should be (1)
    apiResource.models should be (None)

    val api = apiResource.apis.filter(_.path == "/default_param/{id}/").head
    val ops = api.operations

    ops.size should be (1)

    val op = ops.head

    op.responseClass should be ("int")
    op.method should be ("GET")

    op.parameters.size should be (1)

    val param = op.parameters.head

    param.dataType should be ("int")
    param.defaultValue should be (Some("10"))
  }
}

