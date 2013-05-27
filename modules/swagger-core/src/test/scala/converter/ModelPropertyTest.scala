package converter

import com.wordnik.swagger.converter._

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.annotations.ApiProperty

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

import scala.reflect.BeanProperty

@RunWith(classOf[JUnitRunner])
class ModelPropertyTest extends FlatSpec with ShouldMatchers {
  it should "extract properties" in {
    val models = ModelConverters.readAll(classOf[Family])

    models.size should be (3)
    val person = models.filter(m => m.name == "Person").head
    val employer = person.properties("employer")

    employer.`type` should be ("List")
    val employerItems = employer.items.getOrElse(fail("no items found"))
    employerItems.ref should be (Some("Employer"))

    val awards = person.properties("awards")
    awards.`type` should be ("List")
    val awardItems = awards.items.getOrElse(fail("no items found"))
    awardItems.`type` should be ("string")
  }
}

case class Family (membersSince: Date, members: List[Person])

case class Person (
  firstname: String, 
  lastname: String, 
  middlename: Option[String], 
  age: Int, 
  birthday: Date,
  employer: List[Employer],
  awards: List[String])

case class Employer (
  name: String,
  size: Int)