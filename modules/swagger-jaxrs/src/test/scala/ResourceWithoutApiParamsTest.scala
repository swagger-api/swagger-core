import com.wordnik.swagger.jaxrs.reader._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.config._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import testresources.ResourceWithoutApiParams

@RunWith(classOf[JUnitRunner])
class ResourceWithoutApiParamsTest extends FlatSpec with ShouldMatchers {
  it should "read an API without ApiParam annotations" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[ResourceWithoutApiParams], config).getOrElse(fail("should not be None"))
    println(JsonSerializer.asJson(apiResource))
    apiResource.apis.size should be (1)
    val api = apiResource.apis.head
    api.path should be ("/withoutApiParams/{id}")

    val ops = api.operations
    ops.size should be (2)

    val getOp = ops(0)
    getOp.method should be ("GET")
    getOp.parameters.size should be (1)

    val getParam = getOp.parameters(0)
    getParam.name should be ("id")
    getParam.dataType should be ("string")
    getParam.required should be (false)
    getParam.defaultValue should be (Some("1"))

    val putOp = ops(1)
    putOp.method should be ("PUT")
    putOp.parameters.size should be (2)

    val putParam1 = putOp.parameters(0)
    putParam1.name should be ("id")
    putParam1.dataType should be ("string")
    putParam1.required should be (false)

    val putParam2 = putOp.parameters(1)
    putParam2.name should be ("body")
    putParam2.dataType should be ("Howdy")
    putParam2.required should be (false)
  }
}