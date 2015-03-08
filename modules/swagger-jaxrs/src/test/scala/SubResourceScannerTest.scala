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
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithSubResources])
    Json.prettyPrint(swagger)
  }
}
