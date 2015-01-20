import resources._

import com.wordnik.swagger.jaxrs.config._
import com.wordnik.swagger.models.parameters.PathParameter
import com.wordnik.swagger.models.properties.MapProperty

import com.wordnik.swagger.models.Swagger
import com.wordnik.swagger.jaxrs.Reader
import com.wordnik.swagger.util.Json

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class SimpleScannerTest extends FlatSpec with Matchers {
  it should "scan a simple resource" in {
    val swagger = new Reader(new Swagger()).read(classOf[SimpleResource])
    swagger.getPaths().size should be (2)

    val path = swagger.getPaths().get("/{id}")
    val get = path.getGet()
    get should not be (null)
    get.getParameters().size should be (2)

    val param1 = get.getParameters().get(0).asInstanceOf[PathParameter]

    param1.getIn() should be ("path")
    param1.getName() should be ("id")
    param1.getRequired() should be (true)
    param1.getDescription() should be ("sample param data")
    param1.getDefaultValue() should be ("5")

    val param2 = get.getParameters().get(1)
    param2.getIn() should be ("query")
    param2.getName() should be ("limit")
    param2.getRequired() should be (false)
    param2.getDescription() should be (null)
  }

  it should "scan a resource with void return type" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithVoidReturns])
    swagger.getDefinitions().size() should be (1)
    swagger.getDefinitions().get("NotFoundModel") should not be (null)
  }

  it should "scan a resource with map return type" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithMapReturnValue])

    val path = swagger.getPaths().get("/{id}")
    val get = path.getGet()
    get should not be (null)

    get.getResponses() should not be (null)
    val response = get.getResponses().get("200")
    response should not be (null)

    val schema = response.getSchema()
    schema.getClass should be (classOf[MapProperty])
  }

  it should "scan a resource with generics per 653" in {
    val swagger = new Reader(new Swagger()).read(classOf[Resource653])
    val path = swagger.getPaths().get("external/info")
    val get = path.getGet()
    get should not be (null)

    get.getResponses() should not be (null)
    val response = get.getResponses().get("default")

    response should not be (null)
    response.getSchema should be (null)
  }
}
