package converter

import com.wordnik.swagger.converter._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.annotations.ApiModelProperty

import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.util.Date

import scala.beans.BeanProperty

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class PojoConverterTest extends FlatSpec with Matchers {
  Json.mapper().registerModule(DefaultScalaModule)

  ignore should "ignore public fields without annotations" in {
    val models = ModelConverters.getInstance().readAll(classOf[SimplePojo])
    val model = models.get("SimplePojo")
    model.getProperties().size should equal (0)
  }

  it should "sort by annotation hints" in {
    val models = ModelConverters.getInstance().readAll(classOf[SimplePojoWithOrderPreserved])

    val model = models.get("SimplePojoWithOrderPreserved")
    val properties = model.getProperties()
    val itr = properties.keySet().iterator()

    val id = properties.get(itr.next())
    id.getName should be ("id")
    val name = properties.get(itr.next())
    name.getName should be ("name")
    val date = properties.get(itr.next())
    date.getName should be ("date")
  }

  it should "read a simple pojo with BeanAnnotations" in {
    val models = ModelConverters.getInstance().readAll(classOf[SimplePojoWithBeanAnnotations])

    val model = models.get("SimplePojoWithBeanAnnotations")
    val properties = model.getProperties()
    val itr = properties.keySet().iterator()

    val id = properties.get(itr.next())
    id.getName should be ("id")
    val name = properties.get(itr.next())
    name.getName should be ("name")
    val date = properties.get(itr.next())
    date.getName should be ("date")
  }

  it should "read a simple pojo with getters and setters" in {
    val models = ModelConverters.getInstance().readAll(classOf[SimplePojoWithGetterSetters])

    val model = models.get("SimplePojoWithGetterSetters")
    val properties = model.getProperties()
    val itr = properties.keySet().iterator()

    val id = properties.get(itr.next())
    id.getName should be ("id")
    val name = properties.get(itr.next())
    name.getName should be ("name")
    val date = properties.get(itr.next())
    date.getName should be ("date")
  }
}

class SimplePojo {
  var id: Long = 0
  var name: String = _
  var date: Date = _
}

class SimplePojoWithOrderPreserved {
  @ApiModelProperty(position=0) var id: Long = 0
  @ApiModelProperty(position=1) var name: String = _
  @ApiModelProperty(position=2) var date: Date = _
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
