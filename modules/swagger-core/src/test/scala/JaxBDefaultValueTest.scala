import io.swagger.converter.ModelConverters
import io.swagger.util.Json
import models._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JaxBDefaultValueTest extends FlatSpec with Matchers {
  it should "convert a model with Guava optionals" in {
    val schemas = ModelConverters.getInstance().read(classOf[ModelWithJaxBDefaultValues])
    Json.prettyPrint(schemas)
  }
}
