import java.lang.reflect.Method
import javax.ws.rs._

import com.wordnik.swagger.annotations.ApiOperation
import com.wordnik.swagger.jaxrs.Reader
import com.wordnik.swagger.models.Swagger
import javax.ws.rs.core.MediaType
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
}
