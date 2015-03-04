import resources._

import com.wordnik.swagger.jaxrs.config._
import com.wordnik.swagger.models.parameters.PathParameter
import com.wordnik.swagger.models.properties.MapProperty

import com.wordnik.swagger.models.Swagger
import com.wordnik.swagger.jaxrs.Reader
import com.wordnik.swagger.util.Json

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class SimpleScannerTest extends FlatSpec with Matchers {
  it should "scan a simple resource" in {
    val swagger = new Reader(new Swagger()).read(classOf[SimpleResource])
    swagger.getPaths().size should be (2)

    val path = swagger.getPaths().get("/{id}")
    val get = path.getGet()
    get should not be (null)
    get.getParameters().size should be (2)

    val param1 = get.getParameters().get(0).asInstanceOf[PathParameter]

    param1.getIn() should be ("path")
    param1.getName() should be ("id")
    param1.getRequired() should be (true)
    param1.getDescription() should be ("sample param data")
    param1.getDefaultValue() should be ("5")

    val param2 = get.getParameters().get(1)
    param2.getIn() should be ("query")
    param2.getName() should be ("limit")
    param2.getRequired() should be (false)
    param2.getDescription() should be (null)
  }

  it should "scan a resource with void return type" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithVoidReturns])
    swagger.getDefinitions().size() should be (1)
    swagger.getDefinitions().get("NotFoundModel") should not be (null)
  }

  it should "scan a resource with map return type" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithMapReturnValue])

    val path = swagger.getPaths().get("/{id}")
    val get = path.getGet()
    get should not be (null)

    get.getResponses() should not be (null)
    val response = get.getResponses().get("200")
    response should not be (null)

    val schema = response.getSchema()
    schema.getClass should be (classOf[MapProperty])
  }

  it should "scan a resource with generics per 653" in {
    val swagger = new Reader(new Swagger()).read(classOf[Resource653])
    val path = swagger.getPaths().get("/external/info")
    val get = path.getGet()
    get should not be (null)

    get.getResponses() should not be (null)
    val response = get.getResponses().get("default")

    response should not be (null)
    response.getSchema should be (null)
  }

  it should "scan a resource with javax.ws.core.Response " in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithResponse])
    swagger.getDefinitions() should be (null)
  }

  it should "scan a resource with Response.Status return type per 877" in {
    val swagger = new Reader(new Swagger()).read(classOf[Resource877])
    val path = swagger.getPaths().get("/external/info")

    swagger.getTags() should not be (null)
    swagger.getTags().size() should be (1)
    val tag = swagger.getTags().get(0)

    tag.getName() should equal ("externalinfo")
    tag.getDescription() should equal ("it's an api")
    tag.getExternalDocs() should be (null)
  }

  it should "scan a resource with tags" in {
    val swagger = new Reader(new Swagger()).read(classOf[TaggedResource])
    swagger.getTags().size() should be (2)
  }

  it should "scan a resource with tags in test 841" in {
    val swagger = new Reader(new Swagger()).read(classOf[Resource841])
    swagger.getTags().size() should be (3)

    val rootTags = swagger.getPaths().get("/fun").getGet().getTags()
    rootTags.size() should be (2)
    (rootTags.asScala.toList.toSet & Set("tag1", "tag2")).size should be (2)

    val thisTags = swagger.getPaths().get("/fun/this").getGet().getTags()
    thisTags.size() should be (1)
    (thisTags.asScala.toList.toSet & Set("tag1")).size should be (1)

    val thatTags = swagger.getPaths().get("/fun/that").getGet().getTags()
    thatTags.size() should be (1)
    (thatTags.asScala.toList.toSet & Set("tag2")).size should be (1)
  }

  it should "scan a resource with param enums" in {
    val swagger = new Reader(new Swagger()).read(classOf[ResourceWithEnums])
    Json.prettyPrint(swagger)
  }
}
