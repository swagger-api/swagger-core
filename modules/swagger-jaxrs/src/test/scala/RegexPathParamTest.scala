import io.swagger.jaxrs.Reader
import io.swagger.models.Swagger
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import resources._

@RunWith(classOf[JUnitRunner])
class RegexPathParamTest extends FlatSpec with Matchers {
  it should "scan a simple resource" in {
    val swagger = new Reader(new Swagger()).read(classOf[RegexPathParamResource])
    val get = swagger.getPaths().get("/{report_type}").getGet()
    val param = get.getParameters().get(0)
    param.getName() should be("report_type")
    param.getPattern() should be("[aA-zZ]+")
  }
}