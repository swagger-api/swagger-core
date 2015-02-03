package converter

import models._

import com.wordnik.swagger.model._
import com.wordnik.swagger.converter._

import com.wordnik.swagger.core.util._
import com.wordnik.swagger.annotations.ApiModelProperty


import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization.{read, write}

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.beans.BeanProperty

@RunWith(classOf[JUnitRunner])
class SubTypeModelTest extends FlatSpec with Matchers {
  implicit val formats = SwaggerSerializers.formats

  it should "read a model with subTypes" in {
    val model = ModelConverters.read(classOf[Animal]).getOrElse(fail("no model found"))
    model.subTypes.size should be (2)
    write(model) should be ("""{"id":"Animal","description":"a model with subtypes","discriminator":"name","properties":{"name":{"type":"string","description":"name of animal"},"date":{"type":"string","format":"date-time","description":"date added"}},"subTypes":["DomesticAnimal","WildAnimal"]}""")
  }

  it should "read an abstract trait with subTypes" in {
    val models = ModelConverters.readAll(classOf[AbstractBaseModelWithSubTypes])
    models.size should be (3)
    val expectedModels = Seq("AbstractBaseModelWithSubTypes", "Thing1", "Thing2")
    expectedModels.foreach(m => {
      val modelOpt = models.find(md => md.name == m)
      modelOpt should be ('defined)
    })
    val attrADescription = models.find(m => m.name == "Thing1").flatMap(m => m.properties.get("a").flatMap(a => a.description))
    attrADescription should be ('defined)
    attrADescription.get should equal ("Override the abstract a")
  }

  it should "read an abstract trait with subTypes but without fields" in {
    val models = ModelConverters.readAll(classOf[AbstractBaseModelWithoutFields])
    models.size should be (2)
    val expectedModels = Seq("AbstractBaseModelWithoutFields", "Thing3")
    expectedModels.foreach(m => {
      val modelOpt = models.find(md => md.name == m)
      modelOpt should be ('defined)
    })
    val attrADescription = models.find(m => m.name == "Thing3").flatMap(m => m.properties.get("a").flatMap(a => a.description))
    attrADescription should be ('defined)
    attrADescription.get should equal ("Additional field a")
  }

  it should "read a model and read in the subTypes of the model from a field" in {
    val models = ModelConverters.readAll(classOf[ModelWithFieldWithSubTypes])
    models.size should be (4)
    val expectedModels = Seq("ModelWithFieldWithSubTypes", "AbstractBaseModelWithSubTypes", "Thing1", "Thing2")
    expectedModels.foreach(m => {
      val modelOpt = models.find(md => md.name == m)
      modelOpt should be ('defined)
    })
    val attrADescription = models.find(m => m.name == "Thing1").flatMap(m => m.properties.get("a").flatMap(a => a.description))
    attrADescription should be ('defined)
    attrADescription.get should equal ("Override the abstract a")
  }
}
