package testresources

import com.wordnik.swagger.annotations._

import java.util.Date

import javax.ws.rs._
import javax.ws.rs.core.Response

import javax.xml.bind.annotation._

import scala.reflect.BeanProperty

@XmlRootElement (name="Howdy")
@ApiClass(description = "a sample model")
@XmlAccessorType(XmlAccessType.NONE)
class Sample {
  @XmlElement(name="id", required=true)
  @ApiProperty(value = "unique identifier", allowableValues = "1,2,3")
  @BeanProperty var id: String = _

  @XmlElement(name="theName") @BeanProperty var name: String = _
  @XmlElement(name="theValue") @BeanProperty var value: String = _
}

@ApiClass(description = "error response model")
class NotFoundModel {
  var message: String = _
  var code: Int = 0
}

@ApiClass(description = "invalid input model")
case class InvalidInputModel (
  @ApiProperty("the message")message: String,
  @ApiProperty("input error code") code: Int
)

case class Family (membersSince: Date, members: List[Person])

case class Person (
  firstname: String, 
  lastname: String, 
  middlename: Option[String], 
  age: Int, 
  birthday: Date,
  employer: Employer)

case class Employer (
  name: String,
  size: Int)