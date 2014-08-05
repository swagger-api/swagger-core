import models._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models.properties._
import com.wordnik.swagger.converter._

// joda
import org.joda.time.DateTime;

// guava
import com.google.common.base.Optional;

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
    out.isInstanceOf[BooleanProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"boolean"}""")
  }

  ignore should "convert a joda date time" in {
    val out = ModelConverters.readAsProperty(classOf[DateTime])
    println(Json.pretty().writeValueAsString(out))
    out.isInstanceOf[DateTimeProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"string","format":"date-time"}""")
  }

  ignore should "convert a Guava optional" in {
    val out = ModelConverters.readAsProperty(classOf[Optional[Integer]])
    println(Json.pretty().writeValueAsString(out))
    out.isInstanceOf[IntegerProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"integer","format":"int32"}""")
  }

  ignore should "convert a java map" in {
    val out = ModelConverters.read(classOf[java.util.Map[String, String]])
    out.isInstanceOf[MapProperty] should be (true)
    // Json.mapper().writeValueAsString(out) should be ("""{"type":"integer","format":"int32"}""")    
  }

  it should "convert a java string array" in {
    val out = ModelConverters.readAsProperty(classOf[Array[String]])
    out.isInstanceOf[ArrayProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"array","items":{"type":"string"}}""")    
  }

  it should "convert a java integer array" in {
    val out = ModelConverters.readAsProperty(classOf[Array[java.lang.Integer]])
    out.isInstanceOf[ArrayProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"array","items":{"type":"integer","format":"int32"}}""")    
  }

  ignore should "convert a java double array" in {
    val out = ModelConverters.readAsProperty(classOf[Array[java.lang.Double]])
    println(Json.pretty().writeValueAsString(out))
    out.isInstanceOf[ArrayProperty] should be (true)
    Json.mapper().writeValueAsString(out) should be ("""{"type":"array","items":{"type":"number","format":"double"}}""")    
  }
}