import testresources._

import com.wordnik.swagger.jaxrs.reader._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import com.wordnik.swagger.config._
import com.wordnik.swagger.core.filter._

import java.lang.reflect.Method

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class NicknameOverrideTest extends FlatSpec with Matchers {
  it should "read a nickname override" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[NicknameOverride], config).getOrElse(fail("should not be None"))

    apiResource.apis.size should be (1)
    val api = apiResource.apis.head
    api.path should be ("/basic/{id}")

    val ops = api.operations
    ops.size should be (1)

    val op = ops.head
    op.method should be ("GET")
    op.parameters.size should be (1)
    op.nickname should be ("myGetMethod")
    val param = op.parameters.head
    param.name should be ("id")
    param.dataType should be ("string")
    param.required should be (true)
    param.defaultValue should be (Some("1"))
  }
}
