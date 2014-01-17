import com.wordnik.swagger.jersey.JerseyApiReader
import testresources._
import com.wordnik.swagger.config._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

@RunWith(classOf[JUnitRunner])
class SubresourceClassLocatorTest extends FlatSpec with ShouldMatchers {
  it should "find a class subresource" in {
    val reader = new JerseyApiReader
    val config = new SwaggerConfig()
    val apiResource = reader.read("/api-docs", classOf[SubresourceLocatorParentTest], config).getOrElse(fail("should not be None"))

    apiResource.apis should (have length 1)
  }
}