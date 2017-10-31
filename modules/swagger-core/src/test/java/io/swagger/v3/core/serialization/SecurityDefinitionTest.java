package io.swagger.v3.core.serialization;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.oas.models.Person;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.TestUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.testng.annotations.Test;

import java.io.IOException;

public class SecurityDefinitionTest {

    @Test(description = "it should serialize correctly security")
    public void serializeSecurity() throws IOException {
        final OpenAPI oas = TestUtils.deserializeJsonFileFromClasspath("specFiles/securityDefinitions.json", OpenAPI.class);
        final String json = ResourceUtils.loadClassResource(getClass(), "specFiles/securityDefinitions.json");
        SerializationMatchers.assertEqualsToJson(oas, json);
    }

    @Test(description = "it should create a model with security requirements")
    public void createModelWithSecurityRequirements() throws IOException{
        final Schema personModel = ModelConverters.getInstance().read(Person.class).get("Person");
        final Schema errorModel = ModelConverters.getInstance().read(Error.class).get("Error");

        final Info info = new Info()
                .version("1.0.0")
                .title("Swagger Petstore");

        final Contact contact = new Contact()
                .name("Swagger API Team")
                .email("foo@bar.baz")
                .url("http://swagger.io");
        info.setContact(contact);

        final OpenAPI oas = new OpenAPI()
                .info(info)
                .addServersItem(new Server()
                        .url("http://petstore.swagger.io"))
                .schema("Person", personModel)
                .schema("Error", errorModel);

        oas.schemaRequirement("githubAccessCode",
                new SecurityScheme()
                    .flows(new OAuthFlows()
                            .authorizationCode(new OAuthFlow()
                            .scopes(new Scopes().addString("user:email", "Grants read access to a user’s email addresses.")))));

        final Operation get = new Operation()
                .summary("finds pets in the system")
                .description("a longer description")
                .addTagsItem("Pet Operations")
                .operationId("get pet by id");

        get.addParametersItem(new Parameter()
                .in("query")
                .name("tags")
                .description("tags to filter by")
                .required(false)
                .schema(new StringSchema()));
        get.addParametersItem(new Parameter()
                .in("path")
                .name("petId")
                .description("pet to fetch")
                .schema(new IntegerSchema().format("int64")));

        final ApiResponse response = new ApiResponse()
                .description("pets returned")
                .content(new Content().addMediaType("*/*", new MediaType().schema(new Schema().$ref("Person"))));

        final ApiResponse errorResponse = new ApiResponse()
                .description("error response")
                .content(new Content().addMediaType("*/*", new MediaType().schema(new Schema().$ref("Error"))));

        get.responses(new ApiResponses()
                .addApiResponse("200", response)
                .addApiResponse("default", errorResponse))
        .addSecurityItem(new SecurityRequirement()
                .addList("internal_oauth2", "user:email"))
        .addSecurityItem(new SecurityRequirement()
                .addList("api_key"));

        oas.path("/pets", new PathItem().get(get));

        final String json = ResourceUtils.loadClassResource(getClass(), "ModelWithSecurityRequirements.json");
        SerializationMatchers.assertEqualsToJson(oas, json);
    }
}
