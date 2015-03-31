import com.wordnik.swagger.models._

import com.wordnik.swagger.util.Json

import org.junit.runner.RunWith

import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import java.net.URL

import scala.collection.JavaConversions._

@RunWith(classOf[JUnitRunner])
class ResourceListingIT extends FlatSpec with Matchers {
  it should "read a resource listing" in {
    val swagger = Json.mapper().readValue(new URL("http://localhost:8002/api/swagger.json"), classOf[Swagger])

    swagger.getHost() should be ("localhost:8002")
    swagger.getBasePath() should be ("/api")

    val info = swagger.getInfo()
    info should not be (null)
    info.getVersion() should be ("1.0.0")
    info.getTitle() should be ("Petstore sample app")
    info.getContact() should not be (null)
    info.getContact().getName() should be ("apiteam@swagger.io")
    info.getLicense() should not be (null)
    info.getLicense().getName() should be ("Apache 2.0 License")
  }
}
