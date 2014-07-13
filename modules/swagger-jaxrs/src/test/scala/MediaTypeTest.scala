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
class MediaTypeTest extends FlatSpec with Matchers {
  it should "read an api and extract an error model" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[TopLevelMediaTypeResource], config).getOrElse(fail("should not be None"))

    apiResource.consumes.toSet should be (Set("application/json"))
    apiResource.produces.toSet should be (Set("application/json", "application/xml"))
    apiResource.protocols.toSet should be (Set("http", "https"))
  }

  it should "override @Produces annotations with @Api annotation values" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[AnnotationMediaTypeResource], config).getOrElse(fail("should not be None"))

    apiResource.consumes.toSet should be (Set("application/json"))
    apiResource.produces.toSet should be (Set("application/json; charset=utf-8"))
    apiResource.protocols.toSet should be (Set("http", "https"))
  }
}
