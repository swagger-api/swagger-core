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
}