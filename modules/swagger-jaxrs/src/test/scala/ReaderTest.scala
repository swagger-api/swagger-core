import java.lang.reflect.Method
import java.util
import javax.ws.rs._
import javax.ws.rs.core.MediaType

import io.swagger.jaxrs.Reader
import io.swagger.jaxrs.config.ReaderConfig
import io.swagger.models.ModelImpl
import io.swagger.models.Swagger
import io.swagger.models.parameters._
import io.swagger.util.Json
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}
import resources._

@RunWith(classOf[JUnitRunner])
class ReaderTest extends FlatSpec with Matchers {
  it should "scan methods" in {
    val methods = classOf[SimpleMethods].getMethods

    val reader = new Reader(new Swagger())
    for (method <- methods) {
      if (isValidRestPath(method)) {
        val operation = reader.parseMethod(method)
        operation should not be (null)
      }
    }
  }

  it should "scan consumes and produces values from with api class level annotations" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[ApiConsumesProducesResource])
    swagger.getPaths().get("/{id}").getGet.getConsumes.get(0) should equal(MediaType.APPLICATION_XHTML_XML)
    swagger.getPaths().get("/{id}").getGet.getProduces.get(0) should equal(MediaType.APPLICATION_ATOM_XML)
    swagger.getPaths().get("/{id}/value").getGet.getConsumes.get(0) should equal("application/xml")
    swagger.getPaths().get("/{id}/value").getGet.getProduces.get(0) should equal("text/plain")
    swagger.getPaths().get("/{id}").getPut.getConsumes.get(0) should equal(MediaType.APPLICATION_JSON)
    swagger.getPaths().get("/{id}").getPut.getProduces.get(0) should equal({
      "text/plain"
    })
    swagger.getPaths().get("/{id}/value").getPut.getConsumes.get(0) should equal("application/xml")
    swagger.getPaths().get("/{id}/value").getPut.getProduces.get(0) should equal("text/plain")
  }

  it should "scan consumes and produces values with rs class level annotations" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[RsConsumesProducesResource])
    swagger.getPaths().get("/{id}").getGet.getConsumes.get(0) should equal({
      "application/yaml"
    })
    swagger.getPaths().get("/{id}").getGet.getProduces.get(0) should equal({
      "application/xml"
    })
    swagger.getPaths().get("/{id}/value").getGet.getConsumes.get(0) should equal("application/xml")
    swagger.getPaths().get("/{id}/value").getGet.getProduces.get(0) should equal("text/plain")
    swagger.getPaths().get("/{id}").getPut.getConsumes.get(0) should equal(MediaType.APPLICATION_JSON)
    swagger.getPaths().get("/{id}").getPut.getProduces.get(0) should equal({
      "text/plain"
    })
    swagger.getPaths().get("/{id}/value").getPut.getConsumes.get(0) should equal("application/xml")
    swagger.getPaths().get("/{id}/value").getPut.getProduces.get(0) should equal("text/plain")
  }

  it should "scan consumes and produces values with both class level annotations" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[BothConsumesProducesResource])
    swagger.getPaths().get("/{id}").getGet.getConsumes.get(0) should equal(MediaType.APPLICATION_XHTML_XML)
    swagger.getPaths().get("/{id}").getGet.getProduces.get(0) should equal(MediaType.APPLICATION_ATOM_XML)
    swagger.getPaths().get("/{id}/value").getGet.getConsumes.get(0) should equal("application/xml")
    swagger.getPaths().get("/{id}/value").getGet.getProduces.get(0) should equal("text/plain")
    swagger.getPaths().get("/{id}/{name}/value").getGet.getConsumes.get(0) should equal(MediaType.APPLICATION_JSON)
    swagger.getPaths().get("/{id}/{name}/value").getGet.getProduces.get(0) should equal("text/plain")
    swagger.getPaths().get("/{id}/{type}/value").getGet.getConsumes.get(0) should equal("application/xml")
    swagger.getPaths().get("/{id}/{type}/value").getGet.getProduces.get(0) should equal("text/html")
    swagger.getPaths().get("/{id}").getPut.getConsumes.get(0) should equal(MediaType.APPLICATION_JSON)
    swagger.getPaths().get("/{id}").getPut.getProduces.get(0) should equal({
      "text/plain"
    })
    swagger.getPaths().get("/{id}/value").getPut.getConsumes.get(0) should equal("application/xml")
    swagger.getPaths().get("/{id}/value").getPut.getProduces.get(0) should equal("text/plain")
  }

  it should "scan consumes and produces values with no class level annotations" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[NoConsumesProducesResource])
    swagger.getPaths().get("/{id}").getGet.getConsumes should equal(null)
    swagger.getPaths().get("/{id}").getGet.getProduces should equal(null)
    swagger.getPaths().get("/{id}/value").getGet.getConsumes.get(0) should equal("application/xml")
    swagger.getPaths().get("/{id}/value").getGet.getProduces.get(0) should equal("text/plain")
    swagger.getPaths().get("/{id}").getPut.getConsumes.get(0) should equal(MediaType.APPLICATION_JSON)
    swagger.getPaths().get("/{id}").getPut.getProduces.get(0) should equal({
      "text/plain"
    })
    swagger.getPaths().get("/{id}/value").getPut.getConsumes.get(0) should equal("application/xml")
    swagger.getPaths().get("/{id}/value").getPut.getProduces.get(0) should equal("text/plain")
  }

  it should "scan class level and field level annotations" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithKnownInjections])

    val resourceParameters = swagger.getPaths().get("/resource/{id}").getGet().getParameters()
    resourceParameters should not equal (null)
    resourceParameters.size() should equal(3)
    resourceParameters.get(0).asInstanceOf[PathParameter].getName() should equal("id")
    resourceParameters.get(1).asInstanceOf[QueryParameter].getName() should equal("fieldParam")
    resourceParameters.get(2).asInstanceOf[QueryParameter].getName() should equal("methodParam")

    val subResourceParameters1 = swagger.getPaths().get("/resource/{id}/subresource1").getGet().getParameters()
    subResourceParameters1 should not equal (null)
    subResourceParameters1.size() should equal(3)
    subResourceParameters1.get(0).asInstanceOf[PathParameter].getName() should equal("id")
    subResourceParameters1.get(1).asInstanceOf[QueryParameter].getName() should equal("fieldParam")
    subResourceParameters1.get(2).asInstanceOf[QueryParameter].getName() should equal("subResourceParam")

    val subResourceParameters2 = swagger.getPaths().get("/resource/{id}/subresource2").getGet().getParameters()
    subResourceParameters2 should not equal (null)
    subResourceParameters2.size() should equal(4)
    subResourceParameters2.get(0).asInstanceOf[QueryParameter].getName() should equal("subConstructorParam")
    subResourceParameters2.get(1).asInstanceOf[PathParameter].getName() should equal("id")
    subResourceParameters2.get(2).asInstanceOf[QueryParameter].getName() should equal("fieldParam")
    subResourceParameters2.get(3).asInstanceOf[QueryParameter].getName() should equal("subResourceParam")

    val subResourceParameters3 = swagger.getPaths().get("/resource/{id}/subresource3").getGet().getParameters()
    subResourceParameters3 should not equal (null)
    subResourceParameters3.size() should equal(3)
    subResourceParameters3.get(0).asInstanceOf[PathParameter].getName() should equal("id")
    subResourceParameters3.get(1).asInstanceOf[QueryParameter].getName() should equal("fieldParam")
    subResourceParameters3.get(2).asInstanceOf[QueryParameter].getName() should equal("subResourceParam")
  }

  def isValidRestPath(method: Method) = {
    if (Set(Option(method.getAnnotation(classOf[GET])),
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

    swagger.getPaths().get("/pet/{petId6}").getGet should not equal (null)
  }

  it should "scan implicit params" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[ResourceWithImplicitParams])

    swagger.getDefinitions should be (null)

    var params = swagger.getPaths().get("/testString").getPost().getParameters()
    params should not be null
    params.size() should be(7)
    params.get(0).getName() should be("sort")
    params.get(0).getIn() should be("query")
    params.get(1).getName() should be("type")
    params.get(1).getIn() should be("path")
    params.get(2).getName() should be("size")
    params.get(2).getIn() should be("header")
    params.get(3).getName() should be("width")
    params.get(3).getIn() should be("formData")
    params.get(4).getName() should be("width")
    params.get(4).getIn() should be("formData")
    params.get(5).getName() should be("height")
    params.get(5).getIn() should be("query")
    params.get(6).getName() should be("body")
    params.get(6).getIn() should be("body")

    var pathParam: PathParameter = params.get(1).asInstanceOf[PathParameter]
    pathParam.getEnum().size() should be(3)
    pathParam.getType() should be("string")

    var headerParam: HeaderParameter = params.get(2).asInstanceOf[HeaderParameter]
    headerParam.getMinimum() should be(1)

    var formParam: FormParameter = params.get(3).asInstanceOf[FormParameter]
    formParam.getMaximum() should be(1)

    var queryParam: QueryParameter = params.get(5).asInstanceOf[QueryParameter]
    queryParam.getMinimum() should be(3)
    queryParam.getMaximum() should be(4)

    var bodyParam: BodyParameter = params.get(6).asInstanceOf[BodyParameter]
    bodyParam.getRequired() should be(true)
    bodyParam.getSchema.asInstanceOf[ModelImpl].getType should be ("string")
  }

  it should "scan Deprecated annotation" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[ResourceWithDeprecatedMethod])
    swagger.getPaths().get("/testDeprecated").getGet().isDeprecated() should equal(true)
    swagger.getPaths().get("/testAllowed").getGet.isDeprecated() should be(null)
  }
  
  it should "scan Deprecated annotation from interfaceResource" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[DescendantResource])
    var deprecatedMethod = swagger.getPaths().get("/pet/deprecated/{petId7}").getGet
    deprecatedMethod.isDeprecated() should equal(true)
  }

  it should "scan empty path annotation" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[ResourceWithEmptyPath])

    swagger.getPaths().get("/").getGet() should not be(null)

  }

  it should "not scan sub-resource with no @Api annotation when not scanning all resources" in {
    val reader = new Reader(new Swagger())
    val swagger = reader.read(classOf[ResourceWithSubResourceNoApiAnnotation])

    swagger.getPaths().get("/employees/{id}") should be(null)
  }

  it should "scan sub-resource with no @Api annotation when scanning all resources" in {
    val reader = new Reader(new Swagger(), new ReaderConfig {
      override def getIgnoredRoutes: util.Collection[String] = new util.ArrayList[String]()
      override def isScanAllResources: Boolean = true
    })
    val swagger = reader.read(classOf[ResourceWithSubResourceNoApiAnnotation])

    swagger.getPaths().get("/employees/{id}") should not be(null)
  }
}
