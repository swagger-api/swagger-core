import testdata._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._

import play.modules.swagger._

import org.specs2.mutable._
import org.specs2.mock.Mockito


import org.mockito.Mockito._

class EBeanModelTest extends Specification with Mockito {
  "ModelConverters" should {
    "not parse an EBean" in {
      val models = ModelConverters.readAll(classOf[Person])
      models.size must beEqualTo(1)

      val model = models(0)
      model.properties.size must beEqualTo(1)

      val property = model.properties.keys.head
      property must beEqualTo("name")
    }
  }
}