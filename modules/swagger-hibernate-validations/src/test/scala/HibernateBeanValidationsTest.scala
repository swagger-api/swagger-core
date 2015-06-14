// import models.composition._

import io.swagger.converter.ModelConverters
import io.swagger.models.properties.{DoubleProperty, IntegerProperty, StringProperty}
import models._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class HibernateBeanValidationsTest extends FlatSpec with Matchers {
  it should "read hibernate validations" in {
    val schemas = ModelConverters.getInstance().readAll(classOf[HibernateBeanValidationsModel])
    val model = schemas.get("HibernateBeanValidationsModel")
    val properties = model.getProperties()

    val age = properties.get("age").asInstanceOf[IntegerProperty]
    age.getMinimum() should be(13.0)
    age.getMaximum() should be(99.0)

    val password = properties.get("password").asInstanceOf[StringProperty]
    password.getMinLength() should be(6)
    password.getMaxLength() should be(20)

    val minBalance = properties.get("minBalance").asInstanceOf[DoubleProperty]
    minBalance.getExclusiveMinimum().booleanValue() should be(true)

    val maxBalance = properties.get("maxBalance").asInstanceOf[DoubleProperty]
    maxBalance.getExclusiveMaximum().booleanValue() should be(true)
  }
}