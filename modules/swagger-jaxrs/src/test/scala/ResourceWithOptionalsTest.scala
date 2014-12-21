import com.wordnik.swagger.config.SwaggerConfig
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader
import org.junit.runner.RunWith
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.junit.JUnitRunner
import testresources.ResourceWithOptionals

@RunWith(classOf[JUnitRunner])
class ResourceWithOptionalsTest extends FlatSpec with Matchers {
  it should "read an api and extract an error model" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[ResourceWithOptionals], config).getOrElse(fail("should not be None"))

    apiResource.apis.size should be (1)

    val api = apiResource.apis.filter(_.path == "/optional/test").head
    val ops = api.operations
    ops.size should be (1)

    ops.head.parameters.filter(_.paramType == "query").head.dataType should be ("string")

  }
}
