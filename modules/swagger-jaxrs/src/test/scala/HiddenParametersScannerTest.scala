import io.swagger.jaxrs.Reader
import io.swagger.jaxrs.config.DefaultReaderConfig
import io.swagger.models.parameters.{BodyParameter, PathParameter, QueryParameter, SerializableParameter}
import io.swagger.models.properties.{MapProperty, _}
import io.swagger.models.{ArrayModel, Model, ModelImpl, RefModel, Swagger}
import models.TestEnum
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}
import resources._

import scala.collection.JavaConverters._

@RunWith(classOf[JUnitRunner])
class HiddenParametersScannerTest extends FlatSpec with Matchers {
  it should "scan a method with all params hidden" in {
    val swagger = new Reader(new Swagger()).read(classOf[HiddenParametersResource])
    val path = swagger.getPaths().get("/all-hidden/{id}")
    val get = path.getGet()
    get should not be (null)
    get.getParameters().size should be(0)
  }

  it should "scan a method with some params hidden" in {
    val swagger = new Reader(new Swagger()).read(classOf[HiddenParametersResource])
    val path = swagger.getPaths().get("/some-hidden/{id}")
    val get = path.getGet()
    get should not be (null)
    get.getParameters().size should be(3)

    get.getParameters().get(0).getIn() should be("cookie")
    get.getParameters().get(1).getIn() should be("header")
    get.getParameters().get(2).getIn() should be("query")
  }

  it should "scan a method with other params hidden" in {
    val swagger = new Reader(new Swagger()).read(classOf[HiddenParametersResource])
    val path = swagger.getPaths().get("/others-hidden/{id}")
    val get = path.getGet()
    get should not be (null)
    get.getParameters().size should be(3)

    get.getParameters().get(0).getIn() should be("body")
    get.getParameters().get(1).getIn() should be("formData")
    get.getParameters().get(2).getIn() should be("path")
  }
}
