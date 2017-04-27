package converter

import com.wordnik.swagger.converter.ModelConverters
import com.wordnik.swagger.core.util.JsonSerializer
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class EmptyModelTest extends FlatSpec with Matchers {
  it should "read an empty model" in {
    val a = ModelConverters.readAll(classOf[EmptyModel])
    JsonSerializer.asJson(a) should be ("""[{"id":"EmptyModel","properties":{}}]""")
  }

}

case class EmptyModel()