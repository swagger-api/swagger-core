import models._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models.properties._
import com.wordnik.swagger.converter._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class PropertyConverterTest extends FlatSpec with Matchers {
  it should "convert a string" in {
    val out = ModelConverters.readAsProperty(classOf[String])
    out.isInstanceOf[StringProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"string"}""")
  }

  it should "convert a date" in {
    val out = ModelConverters.readAsProperty(classOf[java.util.Date])
    println(Json.mapper().writeValueAsString(out))
    out.isInstanceOf[DateTimeProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"string","format":"date-time"}""")
  }

  it should "convert an Integer" in {
    val out = ModelConverters.readAsProperty(classOf[java.lang.Integer])
    out.isInstanceOf[IntegerProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"integer","format":"int32"}""")
  }

  ignore should "convert a Long" in {
    val out = ModelConverters.readAsProperty(classOf[java.lang.Long])
    out.isInstanceOf[LongProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"integer","format":"int64"}""")
  }

  ignore should "convert a Double" in {
    val out = ModelConverters.readAsProperty(classOf[java.lang.Double])
    println(Json.pretty().writeValueAsString(out))
    out.isInstanceOf[DoubleProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"number","format":"double"}""")
  }

  it should "convert a boolean" in {
    val out = ModelConverters.readAsProperty(classOf[java.lang.Boolean])
    println(Json.pretty().writeValueAsString(out))
    out.isInstanceOf[BooleanProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"boolean"}""")
  }
}