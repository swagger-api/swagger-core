import io.swagger.jaxrs.Reader
import io.swagger.jaxrs.listing.ApiListingResource
import io.swagger.models.Swagger
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner

/**
 * The <code>ApiListingResourceTest</code> test should confirm that scanning of
 * the {@link ApiListingResource} class doesn't affect Swagger output.
 */
@RunWith(classOf[JUnitRunner])
class ApiListingResourceTest extends FlatSpec with Matchers {
  val swagger = new Reader(new Swagger()).read(classOf[ApiListingResource])

  it should "check models set" in {
    swagger.getDefinitions() should be(null)
  }
}
