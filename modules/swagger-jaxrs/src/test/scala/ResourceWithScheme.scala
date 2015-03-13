import resources._

import com.wordnik.swagger.jaxrs.config._
import com.wordnik.swagger.models.Scheme
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
class ResourceWithSchemeTest extends FlatSpec with Matchers {
  it should "scan another resource with subresources" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithScheme])
    val get = swagger.getPaths().get("/test/status").getGet()
    get.getSchemes().get(0) should equal (Scheme.HTTPS)
  }
}
