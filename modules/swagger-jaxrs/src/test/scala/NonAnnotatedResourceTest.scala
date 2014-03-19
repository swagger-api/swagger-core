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
import org.scalatest.matchers.ShouldMatchers

import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class NonAnnotatedResourceTest extends FlatSpec with ShouldMatchers {
  it should "read a resource without Api annotations" in {
    val reader = new BasicJaxrsReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[NonAnnotatedResource], config).getOrElse(fail("should not be None"))

    val apis = apiResource.apis.map(m => (m.path -> m)).toMap

    val api1 = apis("/basic/{id}/r")
    val ops1 = api1.operations.map(m => (m.method -> m)).toMap
    val getOp1 = ops1("GET")
    getOp1.responseClass should be ("void")

    val api2 = apis("/basic/{id}")
    val ops2 = api2.operations.map(m => (m.method -> m)).toMap

    val getOp2 = ops2("GET")
    getOp2.responseClass should be ("Howdy")

    val putOp2 = ops2("PUT")
    putOp2.responseClass should be ("void")

    val models = apiResource.models.get
    val howdy = models("Howdy")    
  }
}
