import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

import com.wordnik.swagger.jaxrs.Reader
import com.wordnik.swagger.jaxrs.listing.ApiListingResource
import com.wordnik.swagger.models.Swagger

/**
 * The <code>ApiListingResourceTest</code> test should confirm that scanning of
 * the {@link ApiListingResource} class doesn't affect Swagger output.
 */
@RunWith(classOf[JUnitRunner])
class ApiListingResourceTest extends FlatSpec with Matchers {
  val swagger = new Reader(new Swagger()).read(classOf[ApiListingResource])

  it should "check models set" in {
    swagger.getDefinitions() should be (null)
  }
}
