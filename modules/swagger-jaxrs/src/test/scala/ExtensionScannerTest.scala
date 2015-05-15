import java.util

import com.wordnik.swagger.jaxrs.Reader
import com.wordnik.swagger.models.Swagger
import com.wordnik.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.{Matchers, FlatSpec}
import org.scalatest.junit.JUnitRunner
import resources.{ResourceWithExtensions}

@RunWith(classOf[JUnitRunner])
class ExtensionScannerTest extends FlatSpec with Matchers {
  it should "scan a resource with extensions" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithExtensions])
    swagger.getPaths().size should be (1)

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
