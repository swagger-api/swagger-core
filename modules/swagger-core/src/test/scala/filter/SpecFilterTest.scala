package filter

import com.wordnik.swagger.util._

import com.wordnik.swagger.core.filter._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.collection.JavaConverters._

@RunWith(classOf[JUnitRunner])
class SpecFilterTest extends FlatSpec with Matchers {
  it should "filter away get operations in a resource" in {
    val swagger = new SwaggerLoader().read("src/test/scala/specFiles/petstore.json")
    val filter = new NoGetOperationsFilter()

    val filtered = new SpecFilter().filter(swagger, filter, null, null, null)

    if(filtered.getPaths() != null) {
      for((path, i) <- filtered.getPaths().asScala) {
        i.getGet() should be (null)
      }
    }
    else
      fail("paths should not be null")
  }

  it should "filter away the store resource" in {
    val swagger = new SwaggerLoader().read("src/test/scala/specFiles/petstore.json")
    val filter = new NoUserOperationsFilter()

    val filtered = new SpecFilter().filter(swagger, filter, null, null, null)

    if(filtered.getPaths() != null) {
      for((path, i) <- filtered.getPaths().asScala) {
        path should not be ("/user")
      }
    }
    else
      fail("paths should not be null")
  }

  it should "filter away secret parameters" in {
    val swagger = new SwaggerLoader().read("src/test/scala/specFiles/sampleSpec.json")
    val filter = new RemoveInternalParamsFilter()

    val filtered = new SpecFilter().filter(swagger, filter, null, null, null)

    if(filtered.getPaths() != null) {
      for((path, i) <- filtered.getPaths().asScala) {
        val get = i.getGet()
        for(param <- get.getParameters().asScala) {
          param.getDescription should not be (null)
          param.getDescription.startsWith("secret") should not be (true)
        }
      }
    }
    else
      fail("paths should not be null")
  }
}