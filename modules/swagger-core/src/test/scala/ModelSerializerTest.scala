import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._
import com.wordnik.swagger.converter._

import com.wordnik.swagger.util.Json

import scala.collection.mutable.HashMap
import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class ModelSerializerTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a model" in {
    val pet = new Model()
    val props = new HashMap[String, Property]
    props += "intValue" -> new IntegerProperty
    props += "longValue" -> new LongProperty
    props += "dateValue" -> new DateProperty
    props += "dateTimeValue" -> new DateTimeProperty
    pet.setProperties(props.asJava)
    pet.setEnum(List("id", "name").asJava)

  }
}
