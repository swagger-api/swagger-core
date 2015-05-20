import java.util

import com.wordnik.swagger.jaxrs.Reader
import com.wordnik.swagger.models.Swagger
import com.wordnik.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.junit.JUnitRunner
import resources.{ResourceWithConfigAndExtensions}

@RunWith(classOf[JUnitRunner])
class ConfigAndExtensionScannerTest extends FlatSpec with Matchers {
  it should "scan a resource with extensions" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithConfigAndExtensions])
    swagger.getPaths().size should be (1)

    var info = swagger.getInfo()
    info should not be null
    info.getDescription() should be ("Custom description")
    info.getTermsOfService() should be ("do-what-you-want")
    info.getTitle() should be ("TheAwesomeApi")
    info.getVersion() should be ("V1.2.3")
    info.getContact().getName() should be ("Sponge-Bob")
    info.getContact().getEmail() should be ("sponge-bob@swagger.io")
    info.getContact().getUrl() should be ("http://swagger.io")
    info.getLicense().getName() should be ("Apache 2.0")
    info.getLicense().getUrl() should be ("http://www.apache.org")

    swagger.getTags().size() should be (3)

    var extensions: util.Map[String, AnyRef] = swagger.getInfo().getVendorExtensions()
    extensions.size() should be (3)
    extensions.get("x-test1") should be ( "value1")
    extensions.get("x-test2") should be ( "value2")

    extensions = extensions.get("x-test").asInstanceOf[util.Map[String, AnyRef]]
    extensions.get("test1") should be ( "value1")
    extensions.get("test2") should be ( "value2")

    extensions = swagger.getPath("/who/cares" ).getOperations().get(0).getVendorExtensions()
    extensions.get("x-test") should be ( "value")

    val json = Json.pretty( swagger )
    json.indexOf( "\"x-test\" : {" ) should not be -1
    json.indexOf( "\"x-test1\" : \"value1\"" ) should not be -1
    json.indexOf( "\"x-test2\" : \"value2\"" ) should not be -1
  }
}
