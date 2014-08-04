import com.wordnik.swagger.jaxrs.config._

import resources.SimpleResource

import com.wordnik.swagger.jaxrs.Reader
import com.wordnik.swagger.util.Json

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class SimpleScannerTest extends FlatSpec with Matchers {
  it should "scan a simple resource" in {
    val swagger = new Reader().read(classOf[SimpleResource])
    swagger.getPaths().size should be (1)

    val path = swagger.getPaths().get("/{id}")
    val get = path.getGet()
    get should not be (null)
    get.getParameters().size should be (2)
    val param1 = get.getParameters().get(0)
    param1.getIn() should be ("path")
    param1.getName() should be ("id")
    param1.getRequired() should be (true)
    param1.getDescription() should be ("sample param data")
    
    val param2 = get.getParameters().get(1)
    param2.getIn() should be ("query")
    param2.getName() should be ("limit")
    param2.getRequired() should be (false)
    param2.getDescription() should be (null)
    println(Json.pretty().writeValueAsString(get))
  }
}