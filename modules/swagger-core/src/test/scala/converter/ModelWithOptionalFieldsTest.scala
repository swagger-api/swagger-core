package converter

import models._

import com.wordnik.swagger.converter._

import com.wordnik.swagger.core.util._
import org.scalatest.{Matchers, FlatSpec}

class ModelWithOptionalFieldsTest extends FlatSpec with Matchers {
  val models = ModelConverters.readAll(classOf[ModelWithOptionalFields])
  JsonSerializer.asJson(models) should be ("""[{"id":"ModelWithOptionalFields","properties":{"string":{"type":"string"},"integer":{"type":"integer","format":"int32"}}}]""")
}
