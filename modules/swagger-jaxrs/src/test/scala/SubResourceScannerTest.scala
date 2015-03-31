import resources._

import models._

import com.wordnik.swagger.jaxrs.config._
import com.wordnik.swagger.models.parameters._
import com.wordnik.swagger.models.properties.MapProperty

import com.wordnik.swagger.models.Swagger
import com.wordnik.swagger.jaxrs.Reader
import com.wordnik.swagger.util.Json

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class SubResourceScannerTest extends FlatSpec with Matchers {
  it should "scan a resource with subresources" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithSubResources])
    Json.prettyPrint(swagger)

    val employees = swagger.getPaths().get("/employees").getGet
    employees.getOperationId() should be ("getTest")

    val employeesId = swagger.getPaths().get("/employees/{id}").getGet
    employeesId.getOperationId() should be ("getSubresourceOperation")
  }

  it should "scan another resource with subresources" in {
    val swagger = new Reader(new Swagger()).read(classOf[TestResource])
    val get = swagger.getPaths().get("/test/more/otherStatus").getGet()
    get.getOperationId() should be ("otherStatus")

    val qp = get.getParameters().get(0)
    qp.getIn() should be ("query")
    qp.getName() should be ("qp")

    val produces = get.getProduces().asScala.toSet
    (produces & Set("application/json", "application/xml")).size should be (2)
    
    swagger.getPaths().keySet().size() should be (2)
  }
}
@RunWith(classOf[JUnitRunner])
class SubResourceScannerTest2 extends FlatSpec with Matchers {
  ignore should "find a body param" in {
    val swagger = new Reader(new Swagger()).read(classOf[Resource942])

  }
}