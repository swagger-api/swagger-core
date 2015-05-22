import io.swagger.jaxrs.Reader
import io.swagger.models.{ModelImpl, Swagger}
import io.swagger.models.properties.MapProperty
import io.swagger.util.Json
import resources._

import io.swagger.jaxrs.config._
import io.swagger.models.parameters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ScannerTest extends FlatSpec with Matchers {
  it should "scan a simple resource" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithBeanParams])
    val get = swagger.getPaths().get("/{id}").getGet()
    val params = get.getParameters()

    val skip = params.get(0)
    skip.getName() should be ("skip")
    skip.getDescription() should be ("number of records to skip")

    val limit = params.get(1)
    limit.getName() should be ("limit")
    limit.getDescription() should be ("maximum number of records to return")
  }

  it should "scan another resource" in {
    val swagger = new Reader( new Swagger()).read( classOf[ResourceWithComplexBodyInputType])

    Json.prettyPrint( swagger )

    val post = swagger.getPaths().get( "/myapi/testPostWithBody" ).getPost()
    post should not be (null)

    swagger.getDefinitions() should not be (null)
    swagger.getDefinitions().get( "ClassWithString" ) should not be (null)
  }

  it should "scan a bean param resource" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithBeanParams])

    val get = swagger.getPaths().get("/bean/{id}").getGet()
    val params = get.getParameters()

    val headerParam1 = params.get(0).asInstanceOf[HeaderParameter]
    headerParam1.getDefaultValue() should be("1")
    headerParam1.getName() should be("test order annotation 1")

    val headerParam2 = params.get(1).asInstanceOf[HeaderParameter]
    headerParam2.getDefaultValue() should be("2")
    headerParam2.getName() should be("test order annotation 2")

    val priority1 = params.get(2).asInstanceOf[QueryParameter]
    priority1.getDefaultValue() should be("overridden")
    priority1.getName() should be("test priority 1")

    val priority2 = params.get(3).asInstanceOf[QueryParameter]
    priority2.getDefaultValue() should be("overridden")
    priority2.getName() should be("test priority 2")

    val bodyParam1 = params.get(4).asInstanceOf[BodyParameter].getSchema().asInstanceOf[ModelImpl]
    bodyParam1.getDefaultValue() should be ("bodyParam")
  }
}
