package converter

import model._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.model._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import scala.reflect.BeanProperty
import scala.collection.mutable.LinkedHashMap
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import javax.xml.bind.annotation._
import converter.models.ATM

@RunWith(classOf[JUnitRunner])
class ATMTest extends FlatSpec with ShouldMatchers {
  it should "read a model with enums" in {
    val a = ModelConverters.readAll(classOf[ATM])
    JsonSerializer.asJson(a) should be ("""[{"id":"ATM","properties":{"currency":{"$ref":"Currency","enum":["USA","CANADA"]}}}]""")
  }
}
