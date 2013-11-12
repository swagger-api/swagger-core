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

@RunWith(classOf[JUnitRunner])
class ATMTest extends FlatSpec with ShouldMatchers {
  it should "read a model with enums" in {
    ModelConverters.readAll(classOf[ATM]) match {
      case a => {
        println(JsonSerializer.asJson(a))
      }
      case _ => fail("didn't read anything")
    }
  }
}
