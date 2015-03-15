// import models.composition._
import models._

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._
import com.wordnik.swagger.converter._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class HibernateBeanValidationsTest extends FlatSpec with Matchers {
  it should "read hibernate validations" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[HibernateBeanValidationsModel])
    val model = schemas.get("HibernateBeanValidationsModel")
    val properties = model.getProperties()

    val age = properties.get("age").asInstanceOf[IntegerProperty]
    age.getMinimum() should be (13.0)
    age.getMaximum() should be (99.0)

    val password = properties.get("password").asInstanceOf[StringProperty]
    password.getMinLength() should be (6)
    password.getMaxLength() should be (20)

    val minBalance = properties.get("minBalance").asInstanceOf[DoubleProperty]
    minBalance.getExclusiveMinimum should be(0.1)

    val maxBalance = properties.get("maxBalance").asInstanceOf[DoubleProperty]
    maxBalance.getExclusiveMaximum should be (1000000.0)
  }
}