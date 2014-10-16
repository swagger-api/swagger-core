import models._

import com.wordnik.swagger.models._
import com.wordnik.swagger.models.properties._
import com.wordnik.swagger.models.parameters._

import com.wordnik.swagger.converter._
import com.wordnik.swagger.util.Json

import scala.collection.mutable.HashMap
import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class SwaggerSerializerTest extends FlatSpec with Matchers {
  val m = Json.mapper()

  it should "convert a spec" in {
    val personModel = ModelConverters.read(classOf[Person]).get("Person")
    val errorModel = ModelConverters.read(classOf[Error]).get("Error")
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
      .host("petstore.swagger.wordnik.com")
      .securityDefinition("api-key", new SecurityDefinition("key"))
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
  }
}
