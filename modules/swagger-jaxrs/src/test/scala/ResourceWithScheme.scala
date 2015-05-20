import resources._

import com.wordnik.swagger.models.{Scheme, Swagger}
import com.wordnik.swagger.jaxrs.Reader

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ResourceWithSchemeTest extends FlatSpec with Matchers {

  val reader = new Reader(new Swagger())

  def getSwagger(resource: Class[_]) = reader.read(resource)
  def loadSchemes(swagger: Swagger, path: String) = swagger.getPaths().get(path).getGet().getSchemes()

  it should "scan another resource with subresources" in {
    val swagger = getSwagger(classOf[ResourceWithScheme])
    loadSchemes(swagger, "/test/status").toArray should equal (Array(Scheme.HTTPS))
    loadSchemes(swagger, "/test/value").toArray should equal (Array(Scheme.WS, Scheme.WSS))
    loadSchemes(swagger, "/test/notes").toArray should equal (Array(Scheme.HTTP))
    loadSchemes(swagger, "/test/description").toArray should equal (Array(Scheme.HTTP))
  }

  it should "scan resource without schemes" in {
    val swagger = getSwagger(classOf[ResourceWithoutScheme])
    loadSchemes(swagger, "/test/status") should equal (null)
    loadSchemes(swagger, "/test/value") should equal (null)
  }
}
