import io.swagger.models.ModelImpl
import io.swagger.models.properties.{LongProperty, StringProperty}
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ModelExampleTest extends FlatSpec with Matchers {
  it should "create a model" in {
    val model = new ModelImpl()
      .property("name", new StringProperty().example("Tony"))
      .property("id", new LongProperty().example(123))
  }

  it should "create a model with example" in {
    val model = new ModelImpl()
      .property("name", new StringProperty().example("Tony"))
      .property("id", new LongProperty().example(123))
      .example( """{"name":"Fred","id":123456"}""")

    model.getExample should equal( """{"name":"Fred","id":123456"}""")
  }
}