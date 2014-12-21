import testresources.ResourceWithReturnTypes

import com.wordnik.swagger.config.SwaggerConfig
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader
import com.wordnik.swagger.core.util.JsonSerializer

import org.junit.runner.RunWith
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ResourceWithReturnTypesTest extends FlatSpec with Matchers {
  it should "read an api and extract the return type" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[ResourceWithReturnTypes], config).getOrElse(fail("should not be None"))

    val operations = apiResource.apis.head.operations

    operations.size should be (1)
    val op = operations.head
    op.responseClass should be ("Howdy")
  }
}
