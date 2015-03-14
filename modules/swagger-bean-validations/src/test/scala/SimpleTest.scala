// import models.composition._
import models._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models._
import com.wordnik.swagger.converter._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class SimpleTest extends FlatSpec with Matchers {
  it should "run well" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[ModelWithBeanValidations])
    Json.prettyPrint(schemas)
  }
}