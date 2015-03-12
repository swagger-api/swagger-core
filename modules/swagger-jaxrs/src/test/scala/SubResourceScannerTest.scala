import resources._

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
    // val swagger = new Reader(new Swagger()).read(classOf[ResourceWithSubResources])
    // Json.prettyPrint(swagger)
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
