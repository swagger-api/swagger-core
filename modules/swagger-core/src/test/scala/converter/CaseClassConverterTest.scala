package converter

import com.wordnik.swagger.converter._

import com.wordnik.swagger.core.util._

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class CaseClassConverterTest extends FlatSpec with ShouldMatchers {
  it should "read a simple case class" in {
  	val model = ModelConverters.read(classOf[SimpleCaseClass]).getOrElse(fail("no model found"))
    model.qualifiedType should be ("converter.SimpleCaseClass")
  	model.properties.map{_._1}.toSet should equal (Set("id", "name", "date"))
  }

  it should "read a nested case class" in {
  	val model = ModelConverters.read(classOf[NestedCaseClass]).getOrElse(fail("no model found"))
    model.qualifiedType should be ("converter.NestedCaseClass")
  	model.properties.map{_._1}.toSet should equal (Set("id", "firstName", "innerObject"))
  }

  it should "read a case class with Options" in {
  	val model = ModelConverters.read(classOf[CaseClassWithOptions]).getOrElse(fail("no model found"))
    model.qualifiedType should be ("converter.CaseClassWithOptions")
    model.properties.map{_._1}.toSet should equal (Set("id", "firstName", "age"))
  }
}

case class SimpleCaseClass(
	id: Long,
	name: String,
	date: Date
)

case class NestedCaseClass(
	id: Long,
	firstName: String,
	innerObject: SimpleCaseClass
)

case class CaseClassWithOptions(
	id: Long,
	firstName: String,
	age: Option[Int]
)
