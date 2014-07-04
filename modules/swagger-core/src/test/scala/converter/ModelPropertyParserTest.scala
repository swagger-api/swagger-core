package converter

import converter.models._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._

import com.wordnik.swagger.annotations.ApiModelProperty

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.beans.BeanProperty

@RunWith(classOf[JUnitRunner])
class ModelPropertyParserTest extends FlatSpec with ShouldMatchers {

  it should "extract a string list" in {
    val cls = classOf[List[String]]
    implicit val properties = new scala.collection.mutable.LinkedHashMap[String, ModelProperty]
    val parser = new ModelPropertyParser(cls)
    parser.parse
  }

  it should "extract enum values from fields" in {
    val cls = classOf[ModelWithEnumField]
    implicit val properties = new scala.collection.mutable.LinkedHashMap[String, ModelProperty]
    val parser = new ModelPropertyParser(cls)
    parser.parse
    val result = properties.get("enumValue").get.allowableValues
    val expectedList = (for(v <- TestEnum.values()) yield v.toString).toList
    assert(AllowableListValues(expectedList) === result)
  }

  it should "extract enum values from method return types" in {
    val cls = classOf[ModelWithEnumProperty]
    implicit val properties = new scala.collection.mutable.LinkedHashMap[String, ModelProperty]
    val parser = new ModelPropertyParser(cls)
    parser.parse
    val result = properties.get("enumValue").get.allowableValues
    val expectedList = (for(v <- TestEnum.values()) yield v.toString).toList
    assert(AllowableListValues(expectedList) === result)
  }
}