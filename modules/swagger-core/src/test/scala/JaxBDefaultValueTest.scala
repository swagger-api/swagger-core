import io.swagger.converter.ModelConverters
import io.swagger.models.ModelImpl
import io.swagger.util.Json
import models._
import models.composition.Pet;
import io.swagger.models.properties._
import io.swagger.converter._

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class JaxBDefaultValueTest extends FlatSpec with Matchers {
  it should "convert a model with Guava optionals" in {
    val schemas = ModelConverters.getInstance().read(classOf[ModelWithJaxBDefaultValues])
    Json.prettyPrint(schemas)
  }
}
