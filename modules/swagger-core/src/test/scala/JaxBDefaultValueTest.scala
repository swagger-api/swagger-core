import io.swagger.converter.ModelConverters
import io.swagger.models.properties.IntegerProperty
import io.swagger.models.properties.StringProperty
import models._

import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

import scala.collection.JavaConverters.mapAsScalaMapConverter

@RunWith(classOf[JUnitRunner])
class JaxBDefaultValueTest extends FlatSpec with Matchers {
  it should "convert a model with Guava optionals" in {
    val schemas = ModelConverters.getInstance().read(classOf[ModelWithJaxBDefaultValues])
    val model = schemas.get("ModelWithJaxBDefaultValues")
    for ((name, property) <- model.getProperties.asScala) {
      name match {
        case "name" =>
          property.asInstanceOf[StringProperty].getDefault should be ("Tony")
        case "age" =>
          property.asInstanceOf[IntegerProperty].getDefault should be (100)
        case _ =>
          fail(s"""Property "${name}" was not expected""")
      }
    }
  }
}
