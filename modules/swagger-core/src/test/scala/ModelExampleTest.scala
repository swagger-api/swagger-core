import models._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

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
      .example("""{"name":"Fred","id":123456"}""")

    model.getExample should equal("""{"name":"Fred","id":123456"}""")
  }
}