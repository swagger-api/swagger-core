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
class PojoConverterTest extends FlatSpec with ShouldMatchers {
  ignore should "ignore public fields without annotations" in {
  	val model = ModelConverters.read(classOf[SimplePojo]).getOrElse(fail("no model found"))
  	model should not be (None)
    model.qualifiedType should be ("converter.SimplePojo")
  	model.properties.size should equal (0)
  }

  it should "sort by annotation hints" in {
  	val model = ModelConverters.read(classOf[SimplePojoWithOrderPreserved]).getOrElse(fail("no model found"))
  	model should not be (None)
    model.qualifiedType should be ("converter.SimplePojoWithOrderPreserved")
  	model.properties.map{_._1}.toList should equal (List("id", "name", "date"))
  }

  it should "read a simple pojo with BeanAnnotations" in {
  	val model = ModelConverters.read(classOf[SimplePojoWithBeanAnnotations]).getOrElse(fail("no model found"))
  	model should not be (None)
    model.qualifiedType should be ("converter.SimplePojoWithBeanAnnotations")
  	model.properties.map{_._1}.toSet should equal (Set("id", "name", "date"))
  }

  it should "read a simple pojo with getters and setters" in {
  	val model = ModelConverters.read(classOf[SimplePojoWithGetterSetters]).getOrElse(fail("no model found"))
  	model should not be (None)
    model.qualifiedType should be ("converter.SimplePojoWithGetterSetters")
  	model.properties.map{_._1}.toSet should equal (Set("id", "name", "date"))
  }
}

class SimplePojo {
	var id: Long = 0
	var name: String = _
	var date: Date = _
}

class SimplePojoWithOrderPreserved {
	@ApiProperty(position=0) var id: Long = 0
	@ApiProperty(position=1) var name: String = _
	@ApiProperty(position=2) var date: Date = _
}

class SimplePojoWithBeanAnnotations {
	@BeanProperty var id: Long = 0
	@BeanProperty var name: String = _
	@BeanProperty var date: Date = _	
}

class SimplePojoWithGetterSetters {
	var id: Long = 0
	var name: String = _
	var date: Date = _

	def getId(): Long = id
	def setId(id: Long) = this.id = id

	def getName(): String = name
	def setName(name: String) = this.name = name

	def getDate(): Date = date
	def setDate(date: Date) = this.date = date
}
