import testresources._

import com.wordnik.swagger.jaxrs.reader._
import com.wordnik.swagger.core.util._
import com.wordnik.swagger.model._
import com.wordnik.swagger.config._
import com.wordnik.swagger.core.filter._
import com.wordnik.swagger.core.util.ReaderUtil

import java.lang.reflect.Method

import java.util.Date

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.collection.mutable.ListBuffer

@RunWith(classOf[JUnitRunner])
class MergedResourceTest extends FlatSpec with Matchers {
  it should "read an api and extract an error model" in {
    val reader = new DefaultJaxrsApiReader
    val config = new SwaggerConfig()
    val r1 = reader.read("/api-docs", classOf[MergedResource1], config).getOrElse(fail("should not be None"))
    val r2 = reader.read("/api-docs", classOf[MergedResource2], config).getOrElse(fail("should not be None"))

    val merged = new TestReader().groupByResourcePath(List(r1, r2))
    merged.size should be (1)
    val listing = merged.head
    (listing.models.get.keys.toSet & Set("NotFoundModel", "Sample1", "Sample2")).size should be (3)
  }
}

class TestReader extends ReaderUtil