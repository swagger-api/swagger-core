import com.wordnik.swagger.jaxrs.config._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models.Scheme

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.collection.JavaConverters._

@RunWith(classOf[JUnitRunner])
class BeanConfigTest extends FlatSpec with Matchers {
  it should "scan a simple resource" in {
    val bc = new BeanConfig()
    bc.setResourcePackage("com.my.project.resources,org.my.project.resources")
    bc.setSchemes(List("http", "https").toArray);
    bc.setHost("petstore.swagger.io")
    bc.setBasePath("/api")
    bc.setTitle("Petstore Sample API")
    bc.setDescription("A sample API that uses a petstore as an example to demonstrate features in the swagger-2.0 specification")
    bc.setTermsOfServiceUrl("http://helloreverb.com/terms/")
    bc.setContact("Wordnik API Team")
    bc.setLicense("MIT")
    bc.setLicenseUrl("http://github.com/gruntjs/grunt/blob/master/LICENSE-MIT")
    bc.setScan(true)

    val swagger = bc.getSwagger()
    swagger should not be (null)
    val keys = swagger.getPaths().keySet()
    keys.asScala.toSet should be (Set("/packageA", "/packageB"))
    val schemes = swagger.getSchemes()
    schemes.asScala.toSet should be (Set(Scheme.HTTP, Scheme.HTTPS))
  }
}