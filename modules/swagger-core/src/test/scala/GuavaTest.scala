import com.fasterxml.jackson.datatype.guava.GuavaModule
import io.swagger.converter.ModelConverters
import io.swagger.util.Json
import matchers.SerializationMatchers.serializeToJson
import models.GuavaModel
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GuavaTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a model with Guava optionals" in {
    m.registerModule(new GuavaModule())

    val schemas = ModelConverters.getInstance().read(classOf[GuavaModel])
    schemas should serializeToJson( """{"GuavaModel":{"type": "object","properties":{"name":{"type":"string"}}}}""")
  }
}
