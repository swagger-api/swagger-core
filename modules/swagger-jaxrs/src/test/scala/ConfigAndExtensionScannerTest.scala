import java.util

import io.swagger.jaxrs.Reader
import io.swagger.models.{Scheme, Swagger}
import io.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}
import resources.ResourceWithConfigAndExtensions

import scala.collection.JavaConversions

@RunWith(classOf[JUnitRunner])
class ConfigAndExtensionScannerTest extends FlatSpec with Matchers {
  it should "scan a resource with extensions" in {
    var classes: Set[Class[_]] = Set(classOf[ResourceWithConfigAndExtensions])
    val swagger = new Reader(new Swagger()).read(JavaConversions.setAsJavaSet(classes))
    swagger.getPaths().size should be(1)

    var info = swagger.getInfo()
    info should not be null
    info.getDescription() should be("Custom description")
    info.getTermsOfService() should be("do-what-you-want")
    info.getTitle() should be("TheAwesomeApi")
    info.getVersion() should be("V1.2.3")
    info.getContact().getName() should be("Sponge-Bob")
    info.getContact().getEmail() should be("sponge-bob@swagger.io")
    info.getContact().getUrl() should be("http://swagger.io")
    info.getLicense().getName() should be("Apache 2.0")
    info.getLicense().getUrl() should be("http://www.apache.org")

    swagger.getConsumes().size() should be(2)
    swagger.getConsumes().contains("application/json")
    swagger.getConsumes().contains("application/xml")

    swagger.getProduces().size() should be(2)
    swagger.getProduces().contains("application/json")
    swagger.getProduces().contains("application/xml")

    swagger.getExternalDocs().getDescription() should be("test")
    swagger.getExternalDocs().getUrl() should be("http://swagger.io")

    swagger.getSchemes().size() should be(2)
    swagger.getSchemes().contains(Scheme.HTTP);
    swagger.getSchemes().contains(Scheme.HTTPS);

    swagger.getTags().size() should be(6)
    swagger.getTags().get(0).getName() should be("Tag-added-before-read")

    swagger.getTags().get(1).getName() should be("mytag")
    swagger.getTags().get(1).getDescription() should be("my tag")

    swagger.getTags().get(2).getName() should be("anothertag")
    swagger.getTags().get(2).getDescription() should be("another tag")
    swagger.getTags().get(2).getExternalDocs().getDescription() should be("test")
    swagger.getTags().get(2).getExternalDocs().getUrl() should be("http://swagger.io")

    swagger.getTags().get(3).getName() should be("tagwithextensions")
    swagger.getTags().get(3).getDescription() should be("my tag")
    var extensions: util.Map[String, AnyRef] = swagger.getTags().get(3).getVendorExtensions()
    extensions.size() should be(1)
    extensions.get("x-test") should be("value")

    swagger.getTags().get(4).getName() should be("externalinfo")
    swagger.getTags().get(5).getName() should be("Tag-added-after-read")

    extensions = swagger.getInfo().getVendorExtensions()
    extensions.size() should be(3)
    extensions.get("x-test1") should be("value1")
    extensions.get("x-test2") should be("value2")

    extensions = extensions.get("x-test").asInstanceOf[util.Map[String, AnyRef]]
    extensions.get("test1") should be("value1")
    extensions.get("test2") should be("value2")

    extensions = swagger.getPath("/who/cares").getOperations().get(0).getVendorExtensions()
    extensions.get("x-test") should be("value")

    val json = Json.pretty(swagger)
    json.indexOf("\"x-test\" : {") should not be -1
    json.indexOf("\"x-test1\" : \"value1\"") should not be -1
    json.indexOf("\"x-test2\" : \"value2\"") should not be -1
  }
}
