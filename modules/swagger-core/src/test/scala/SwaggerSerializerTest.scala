import models._

import com.wordnik.swagger.models._
import com.wordnik.swagger.models.auth._
import com.wordnik.swagger.models.properties._
import com.wordnik.swagger.models.parameters._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.util.Json

import scala.collection.mutable.HashMap
import scala.collection.JavaConverters._
import scala.io.Source

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class SwaggerSerializerTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a spec" in {
    val personModel = ModelConverters.getInstance().read(classOf[Person]).get("Person")
    val errorModel = ModelConverters.getInstance().read(classOf[Error]).get("Error")
    val info = new Info()
      .version("1.0.0")
      .title("Swagger Petstore")

    val contact = new Contact()
      .name("Wordnik API Team")
      .email("foo@bar.baz")
      .url("http://swagger.io")
    info.setContact(contact)

    val swagger = new Swagger()
      .info(info)
      .host("petstore.swagger.io")
      .securityDefinition("api-key", new ApiKeyAuthDefinition("key", In.HEADER))
      .scheme(Scheme.HTTP)
      .consumes("application/json")
      .produces("application/json")
      .model("Person", personModel)

    val get = new Operation()
      .produces("application/json")
      .summary("finds pets in the system")
      .description("a longer description")
      .tag("Pet Operations")
      .operationId("get pet by id")
      .deprecated(true)

    get.parameter(new QueryParameter()
      .name("tags")
      .description("tags to filter by")
      .required(false)
      .property(new StringProperty())
    )
    get.parameter(new PathParameter()
      .name("petId")
      .description("pet to fetch")
      .property(new LongProperty())
    )

    val response = new Response()
      .description("pets returned")
      .schema(new RefProperty().asDefault("Person"))
      .example("application/json", "fun!")

    val errorResponse = new Response()
      .description("error response")
      .schema(new RefProperty().asDefault("Error"))

    get.response(200, response)
      .defaultResponse(errorResponse)

    val post = new Operation()
      .summary("adds a new pet")
      .description("you can add a new pet this way")
      .tag("Pet Operations")
      .operationId("add pet")
      .defaultResponse(errorResponse)
      .parameter(new BodyParameter()
        .description("the pet to add")
        .schema(new RefModel().asDefault("Person")))

    swagger.path("/pets", new Path().get(get).post(post))
    val swaggerJson = Json.mapper().writeValueAsString(swagger)
    val rebuilt = Json.mapper().readValue(swaggerJson, classOf[Swagger])
    Json.pretty(swagger) should equal (Json.pretty(rebuilt))
  }

  it should "read the uber api" in {
    val jsonString = Source.fromFile("src/test/scala/specFiles/uber.json").mkString.toString
    val swagger = Json.mapper().readValue(jsonString, classOf[Swagger])
  }

  it should "write a spec with parameter references" in {
    val personModel = ModelConverters.getInstance().read(classOf[Person]).get("Person")

    val info = new Info()
      .version("1.0.0")
      .title("Swagger Petstore")

    val contact = new Contact()
      .name("Wordnik API Team")
      .email("foo@bar.baz")
      .url("http://swagger.io")
    info.setContact(contact)

    val swagger = new Swagger()
      .info(info)
      .host("petstore.swagger.io")
      .securityDefinition("api-key", new ApiKeyAuthDefinition("key", In.HEADER))
      .scheme(Scheme.HTTP)
      .consumes("application/json")
      .produces("application/json")
      .model("Person", personModel)

    val parameter = new QueryParameter()
      .name("id")
      .description("a common get parameter")
      .property(new LongProperty())

    val get = new Operation()
      .produces("application/json")
      .summary("finds pets in the system")
      .description("a longer description")
      .tag("Pet Operations")
      .operationId("get pet by id")
      .parameter(new RefParameter("foo"))

    swagger
      .parameter("foo", parameter)
      .path("/pets", new Path().get(get))

    val swaggerJson = Json.mapper().writeValueAsString(swagger)
    val rebuilt = Json.mapper().readValue(swaggerJson, classOf[Swagger])
    Json.pretty(swagger) should equal (Json.pretty(rebuilt))
  }
}
