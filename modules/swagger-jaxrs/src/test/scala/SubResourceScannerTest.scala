import io.swagger.jaxrs.Reader
import io.swagger.models.Swagger
import io.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import resources._

import scala.collection.JavaConverters._

@RunWith(classOf[JUnitRunner])
class SubResourceScannerTest extends FlatSpec with Matchers {
  it should "scan a resource with subresources" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithSubResources])
    Json.prettyPrint(swagger)

    val employees = swagger.getPaths().get("/employees").getGet
    employees.getOperationId() should be("getTest")

    val employeesId = swagger.getPaths().get("/employees/{id}").getGet
    employeesId.getOperationId() should be("getSubresourceOperation")

    val noPath = swagger.getPath("/employees/noPath").getGet
    noPath.getOperationId() should be("getGreeting")
  }

  it should "scan another resource with subresources" in {
    val swagger = new Reader(new Swagger()).read(classOf[TestResource])
    val get = swagger.getPaths().get("/test/more/otherStatus").getGet()
    get.getOperationId() should be("otherStatus")

    val qp = get.getParameters().get(0)
    qp.getIn() should be("query")
    qp.getName() should be("qp")

    val produces = get.getProduces().asScala.toSet
    (produces & Set("application/json", "application/xml")).size should be(2)

    swagger.getPaths().keySet().size() should be(2)
  }

  it should "scan resource with class-based sub-resources" in {
    val swagger = new Reader(new Swagger()).read(classOf[SubResourceHead])

    swagger.getPaths().size() should be(3)

    val noPath = swagger.getPath("/head/noPath").getGet()
    noPath.getOperationId() should be("getGreeting")

    val hello = swagger.getPath("/head/tail/hello").getGet()
    hello.getOperationId() should be("getGreeting")

    val echo = swagger.getPath("/head/tail/{string}").getGet()
    echo.getOperationId() should be("getEcho")
    echo.getParameters().size() should be(1)
  }
}

@RunWith(classOf[JUnitRunner])
class SubResourceScannerTest2 extends FlatSpec with Matchers {
  ignore should "find a body param" in {
    val swagger = new Reader(new Swagger()).read(classOf[Resource942])

  }
}
