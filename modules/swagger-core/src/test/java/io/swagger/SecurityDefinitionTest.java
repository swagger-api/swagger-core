package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Person;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.Error;
import io.swagger.models.SecurityRequirement;
import io.swagger.models.Swagger;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.ResourceUtils;

import org.testng.annotations.Test;

import java.io.IOException;

public class SecurityDefinitionTest {

    @Test(description = "it should create a model with security requirements")
    public void createModelWithSecurityRequirements() throws IOException{
        final Model personModel = ModelConverters.getInstance().read(Person.class).get("Person");
        final Model errorModel = ModelConverters.getInstance().read(Error.class).get("Error");

        final Info info = new Info()
                .version("1.0.0")
                .title("Swagger Petstore");

        final Contact contact = new Contact()
                .name("Swagger API Team")
                .email("foo@bar.baz")
                .url("http://swagger.io");
        info.setContact(contact);

        final Swagger swagger = new Swagger()
                .info(info)
                .host("petstore.swagger.io")
                .scheme(Scheme.HTTP)
                .consumes("application/json")
                .produces("application/json")
                .model("Person", personModel)
                .model("Error", errorModel);

        swagger.securityDefinition("githubAccessCode",
                new OAuth2Definition()
                        .accessCode("http://foo.com/accessCode", "http://foo.com/tokenUrl")
                        .scope("user:email", "Grants read access to a user’s email addresses."));

        final Operation get = new Operation()
                .produces("application/json")
                .summary("finds pets in the system")
                .description("a longer description")
                .tag("Pet Operations")
                .operationId("get pet by id");

        get.parameter(new QueryParameter()
                        .name("tags")
                        .description("tags to filter by")
                        .required(false)
                        .property(new StringProperty())
        );
        get.parameter(new PathParameter()
                        .name("petId")
                        .description("pet to fetch")
                        .property(new LongProperty())
        );

        final Response response = new Response()
                .description("pets returned")
                .schema(new RefProperty().asDefault("Person"));

        final Response errorResponse = new Response()
                .description("error response")
                .schema(new RefProperty().asDefault("Error"));

        get.response(200, response)
                .defaultResponse(errorResponse)
                .security(new SecurityRequirement("internal_oauth2")
                        .scope("user:email"))
                .security(new SecurityRequirement("api_key"));

        swagger.path("/pets", new Path().get(get));

        final String json = ResourceUtils.loadClassResource(getClass(), "ModelWithSecurityRequirements.json");
        SerializationMatchers.assertEqualsToJson(swagger, json);
    }
}
