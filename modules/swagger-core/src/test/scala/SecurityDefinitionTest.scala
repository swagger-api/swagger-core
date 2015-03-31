import models._
import models.composition.Pet;

import com.wordnik.swagger.util.Json
import com.wordnik.swagger.models._
import com.wordnik.swagger.models.auth._
import com.wordnik.swagger.converter._
import com.wordnik.swagger.models.parameters._
import com.wordnik.swagger.models.properties._

import scala.collection.JavaConverters._

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FlatSpec
import org.scalatest.Matchers

@RunWith(classOf[JUnitRunner])
class SecurityDefinitionTest extends FlatSpec with Matchers {
  it should "create a model with security requirements" in {
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
      .scheme(Scheme.HTTP)
      .consumes("application/json")
      .produces("application/json")
      .model("Person", personModel)

    swagger
      .securityDefinition("githubAccessCode",
        new OAuth2Definition()
          .accessCode("http://foo.com/accessCode", "http://foo.com/tokenUrl")
          .scope("user:email", "Grants read access to a user’s email addresses."))

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
      .security(new SecurityRequirement("internal_oauth2")
        .scope("user:email"))
      .security(new SecurityRequirement("api_key"))

    swagger.path("/pets", new Path().get(get))

    val json = Json.mapper.writeValueAsString(swagger)
  }
}
