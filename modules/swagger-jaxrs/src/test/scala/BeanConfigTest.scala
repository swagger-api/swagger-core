import com.wordnik.swagger.jaxrs.config._

import com.wordnik.swagger.util.Json

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class BeanConfigTest extends FlatSpec with Matchers {
  it should "scan a simple resource" in {
    val bc = new BeanConfig()
    bc.setResourcePackage("resources")

    bc.setHost("petstore.swagger.wordnik.com")
    bc.setBasePath("/api")
    bc.setTitle("Petstore Sample API")
    bc.setDescription("A sample API that uses a petstore as an example to demonstrate features in the swagger-2.0 specification")
    bc.setTermsOfServiceUrl("http://helloreverb.com/terms/")
    bc.setContact("Wordnik API Team")
    bc.setLicense("MIT")
    bc.setLicenseUrl("http://github.com/gruntjs/grunt/blob/master/LICENSE-MIT")
    bc.setScan(true)

    println(Json.pretty().writeValueAsString(bc.getSwagger()))
  }
}