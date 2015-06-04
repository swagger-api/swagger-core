import java.lang.reflect.Method
import javax.ws.rs._
import javax.ws.rs.core.MediaType
import com.wordnik.swagger.models.parameters._
import io.swagger.annotations.ApiOperation
import io.swagger.jaxrs.Reader
import io.swagger.models.Swagger
import io.swagger.models.parameters._
import org.junit.runner.RunWith
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.junit.JUnitRunner
import resources._

@RunWith(classOf[JUnitRunner])
class ReaderTest extends FlatSpec with Matchers {
  it should "scan methods" in {
    val methods = classOf[SimpleMethods].getMethods

    val reader = new Reader(new Swagger())
    for(method <- methods) {
      if(isValidRestPath(method)) {
        val operation = reader.parseMethod(method)
        operation should not be (null)
      }
    }
  }

  it should "scan consumes and produces values from with api class level annotations" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[ApiConsumesProducesResource])
    swagger.getPaths().get("/{id}").getGet.getConsumes.get(0) should equal (MediaType.APPLICATION_XHTML_XML)
    swagger.getPaths().get("/{id}").getGet.getProduces.get(0) should equal (MediaType.APPLICATION_ATOM_XML)
    swagger.getPaths().get("/{id}/value").getGet.getConsumes.get(0) should equal ("application/xml")
    swagger.getPaths().get("/{id}/value").getGet.getProduces.get(0) should equal ("text/plain")
    swagger.getPaths().get("/{id}").getPut.getConsumes.get(0) should equal (MediaType.APPLICATION_JSON)
    swagger.getPaths().get("/{id}").getPut.getProduces.get(0) should equal ({"text/plain"})
    swagger.getPaths().get("/{id}/value").getPut.getConsumes.get(0) should equal ("application/xml")
    swagger.getPaths().get("/{id}/value").getPut.getProduces.get(0) should equal ("text/plain")
  }

  it should "scan consumes and produces values with rs class level annotations" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[RsConsumesProducesResource])
    swagger.getPaths().get("/{id}").getGet.getConsumes.get(0) should equal ({"application/yaml"})
    swagger.getPaths().get("/{id}").getGet.getProduces.get(0) should equal ({"application/xml"})
    swagger.getPaths().get("/{id}/value").getGet.getConsumes.get(0) should equal ("application/xml")
    swagger.getPaths().get("/{id}/value").getGet.getProduces.get(0) should equal ("text/plain")
    swagger.getPaths().get("/{id}").getPut.getConsumes.get(0) should equal (MediaType.APPLICATION_JSON)
    swagger.getPaths().get("/{id}").getPut.getProduces.get(0) should equal ({"text/plain"})
    swagger.getPaths().get("/{id}/value").getPut.getConsumes.get(0) should equal ("application/xml")
    swagger.getPaths().get("/{id}/value").getPut.getProduces.get(0) should equal ("text/plain")
  }

  it should "scan consumes and produces values with both class level annotations" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[BothConsumesProducesResource])
    swagger.getPaths().get("/{id}").getGet.getConsumes.get(0) should equal (MediaType.APPLICATION_XHTML_XML)
    swagger.getPaths().get("/{id}").getGet.getProduces.get(0) should equal (MediaType.APPLICATION_ATOM_XML)
    swagger.getPaths().get("/{id}/value").getGet.getConsumes.get(0) should equal ("application/xml")
    swagger.getPaths().get("/{id}/value").getGet.getProduces.get(0) should equal ("text/plain")
    swagger.getPaths().get("/{id}/{name}/value").getGet.getConsumes.get(0) should equal (MediaType.APPLICATION_JSON)
    swagger.getPaths().get("/{id}/{name}/value").getGet.getProduces.get(0) should equal ("text/plain")
    swagger.getPaths().get("/{id}/{type}/value").getGet.getConsumes.get(0) should equal ("application/xml")
    swagger.getPaths().get("/{id}/{type}/value").getGet.getProduces.get(0) should equal ("text/html")
    swagger.getPaths().get("/{id}").getPut.getConsumes.get(0) should equal (MediaType.APPLICATION_JSON)
    swagger.getPaths().get("/{id}").getPut.getProduces.get(0) should equal ({"text/plain"})
    swagger.getPaths().get("/{id}/value").getPut.getConsumes.get(0) should equal ("application/xml")
    swagger.getPaths().get("/{id}/value").getPut.getProduces.get(0) should equal ("text/plain")
  }

  it should "scan consumes and produces values with no class level annotations" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[NoConsumesProducesResource])
    swagger.getPaths().get("/{id}").getGet.getConsumes should equal (null)
    swagger.getPaths().get("/{id}").getGet.getProduces should equal (null)
    swagger.getPaths().get("/{id}/value").getGet.getConsumes.get(0) should equal ("application/xml")
    swagger.getPaths().get("/{id}/value").getGet.getProduces.get(0) should equal ("text/plain")
    swagger.getPaths().get("/{id}").getPut.getConsumes.get(0) should equal (MediaType.APPLICATION_JSON)
    swagger.getPaths().get("/{id}").getPut.getProduces.get(0) should equal ({"text/plain"})
    swagger.getPaths().get("/{id}/value").getPut.getConsumes.get(0) should equal ("application/xml")
    swagger.getPaths().get("/{id}/value").getPut.getProduces.get(0) should equal ("text/plain")
  }

  def isValidRestPath(method: Method) = {
    if(Set(Option(method.getAnnotation(classOf[GET])),
      Option(method.getAnnotation(classOf[PUT])),
      Option(method.getAnnotation(classOf[POST])),
      Option(method.getAnnotation(classOf[DELETE])),
      Option(method.getAnnotation(classOf[OPTIONS])),
      Option(method.getAnnotation(classOf[HEAD]))).flatten.size > 0)
      true
    else
      false
  }

  it should "scan overridden method in descendantResource" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[DescendantResource])
    val overriddenMethodWithTypedParam = swagger.getPaths().get("/pet/{petId1}").getGet
    overriddenMethodWithTypedParam should not equal (null)
    overriddenMethodWithTypedParam.getParameters.get(0).getDescription should equal("ID of pet to return child")

    val methodWithoutTypedParam = swagger.getPaths().get("/pet/{petId2}").getGet
    methodWithoutTypedParam should not equal (null)

    val overriddenMethodWithoutTypedParam = swagger.getPaths().get("/pet/{petId3}").getGet
    overriddenMethodWithoutTypedParam should not equal (null)

    val methodWithoutTypedParamFromDescendant = swagger.getPaths().get("/pet/{petId4}").getGet
    methodWithoutTypedParamFromDescendant should not equal (null)

    val methodFromInterface = swagger.getPaths().get("/pet/{petId5}").getGet
    methodFromInterface should not equal (null)
  }

  it should "scan implicit params" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[ResourceWithImplicitParams])

    var params = swagger.getPaths().get( "/testString").getPost().getParameters()
    params should not be null
    params.size() should be (7)
    params.get(0).getName() should be ("sort")
    params.get(0).getIn() should be ("query")
    params.get(1).getName() should be ("type")
    params.get(1).getIn() should be ("path")
    params.get(2).getName() should be ("size")
    params.get(2).getIn() should be ("header")
    params.get(3).getName() should be ("width")
    params.get(3).getIn() should be ("formData")
    params.get(4).getName() should be ("width")
    params.get(4).getIn() should be ("formData")
    params.get(5).getName() should be ("height")
    params.get(5).getIn() should be ("query")
    params.get(6).getName() should be ("body")
    params.get(6).getIn() should be ("body")

    var pathParam : PathParameter = params.get(1).asInstanceOf[PathParameter]
    pathParam.getEnum().size() should be (3)
    pathParam.getType() should be ("string")

    var headerParam : HeaderParameter = params.get(2).asInstanceOf[HeaderParameter]
    headerParam.getMinimum() should be (1)

    var formParam : FormParameter = params.get(3).asInstanceOf[FormParameter]
    formParam.getMaximum() should be (1)

    var queryParam : QueryParameter = params.get(5).asInstanceOf[QueryParameter]
    queryParam.getMinimum() should be (3)
    queryParam.getMaximum() should be (4)

    var bodyParam : BodyParameter = params.get(6).asInstanceOf[BodyParameter]
    bodyParam.getRequired() should be (true)
  }
}
