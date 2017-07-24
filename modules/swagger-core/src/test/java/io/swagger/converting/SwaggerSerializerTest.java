package io.swagger.converting;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.oas.models.Components;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.Paths;
import io.swagger.oas.models.Person;
import io.swagger.oas.models.info.Contact;
import io.swagger.oas.models.info.Info;
import io.swagger.oas.models.links.Link;
import io.swagger.oas.models.media.Content;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.MediaType;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import io.swagger.oas.models.parameters.Parameter;
import io.swagger.oas.models.parameters.QueryParameter;
import io.swagger.oas.models.parameters.RequestBody;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.oas.models.responses.ApiResponses;
import io.swagger.oas.models.servers.Server;
import io.swagger.util.Json;
import io.swagger.util.OutputReplacer;
import io.swagger.util.ResourceUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class SwaggerSerializerTest {
    ObjectMapper m = Json.mapper();

    @Test(description = "it should convert a spec")
    public void convertSpec() throws IOException {
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

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "value");
        info.addExtension("x-test2", map);
        info.addExtension("x-test", "value");

        final OpenAPI swagger = new OpenAPI()
                .info(info)
                .addServersItem(new Server()
                        .url("http://petstore.swagger.io"))

//                .securityDefinition("api-key", new ApiKeyAuthDefinition("key", In.HEADER))
//                .consumes("application/json")
//                .produces("application/json")
                .schema("Person", personModel)
                .schema("Error", errorModel);

        final Operation get = new Operation()
//                .produces("application/json")
                .summary("finds pets in the system")
                .description("a longer description")
                .addTagsItem("Pet Operations")
                .operationId("get pet by id")
                .deprecated(true);

        get.addParametersItem(new Parameter()
                .in("query")
                .name("tags")
                .description("tags to filter by")
                .required(false)
                .schema(new StringSchema())
        );

        get.addParametersItem(new Parameter()
                .in("path")
                .name("petId")
                .description("pet to fetch")
                .schema(new IntegerSchema().format("int64"))
        );

        final ApiResponse response = new ApiResponse()
                .description("pets returned")
                .content(new Content()
                    .addMediaType("application/json", new io.swagger.oas.models.media.MediaType()
                    .schema(new Schema().$ref("Person"))
                    .example("fun")));

        final ApiResponse errorResponse = new ApiResponse()
                .description("error response")
                .link("myLink", new Link()
                        .description("a link")
                        .operationId("theLinkedOperationId")
                        .parameters("userId", "gah")
                )
                .content(new Content()
                        .addMediaType("application/json", new io.swagger.oas.models.media.MediaType()
                        .schema(new Schema().$ref("Error"))));

        get.responses(new ApiResponses()
                .addApiResponse("200", response)
                .addApiResponse("default", errorResponse));

        final Operation post = new Operation()
                .summary("adds a new pet")
                .description("you can add a new pet this way")
                .addTagsItem("Pet Operations")
                .operationId("add pet")
                .responses(new ApiResponses()
                        .addApiResponse("default",errorResponse))
                .requestBody(new RequestBody()
                        .description("the pet to add")
                        .content(new Content().addMediaType("*/*", new MediaType()
                        .schema(new Schema().$ref("Person")))));

        swagger.paths(new Paths().addPathItem("/pets", new PathItem()
                .get(get).post(post)));
        final String swaggerJson = Json.mapper().writeValueAsString(swagger);
        Json.prettyPrint(swagger);
        final OpenAPI rebuilt = Json.mapper().readValue(swaggerJson, OpenAPI.class);
        SerializationMatchers.assertEqualsToJson(rebuilt, swaggerJson);
    }
    @Test(description = "it should read the uber api")
    public void readUberApi() throws IOException {
        final String jsonString = ResourceUtils.loadClassResource(getClass(), "uber.json");
        final OpenAPI swagger = Json.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
    }


    @Test(description = "it should write a spec with parameter references")
    public void writeSpecWithParameterReferences() throws IOException {
        final Schema personModel = ModelConverters.getInstance().read(Person.class).get("Person");

        final Info info = new Info()
                .version("1.0.0")
                .title("Swagger Petstore");

        final Contact contact = new Contact()
                .name("Swagger API Team")
                .email("foo@bar.baz")
                .url("http://swagger.io");
        info.setContact(contact);

        final OpenAPI swagger = new OpenAPI()
                .info(info)
                .addServersItem(new Server().url("http://petstore.swagger.io"))
                //.consumes("application/json")
                //.produces("application/json")
                .schema("Person", personModel);

        final QueryParameter parameter = (QueryParameter)new QueryParameter()
                .name("id")
                .description("a common get parameter")
                .schema(new IntegerSchema());

        final Operation get = new Operation()
                //.produces("application/json")
                .summary("finds pets in the system")
                .description("a longer description")
                //.tag("Pet Operations")
                .operationId("get pet by id")
                .addParametersItem(new Parameter().$ref("#/parameters/Foo"));


        swagger
                .components(new Components().addParameters("Foo", parameter))
                .path("/pets", new PathItem().get(get));

        final String swaggerJson = Json.mapper().writeValueAsString(swagger);
        final OpenAPI rebuilt = Json.mapper().readValue(swaggerJson, OpenAPI.class);
        assertEquals(Json.pretty(rebuilt), Json.pretty(swagger));
    }

    @Test
    public void prettyPrintTest() throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), "uber.json");
        final OpenAPI swagger = Json.mapper().readValue(json, OpenAPI.class);
        final String outputStream = OutputReplacer.OUT.run(new OutputReplacer.Function() {
            @Override
            public void run() {
                Json.prettyPrint(swagger);
            }
        });
        SerializationMatchers.assertEqualsToJson(swagger, outputStream);
    }

    @Test
    public void exceptionsTest() throws IOException {
        final String outputStream1 = OutputReplacer.ERROR.run(new OutputReplacer.Function() {
            @Override
            public void run() {
                Json.pretty(new ThrowHelper());
            }
        });
        assertTrue(outputStream1.contains(ThrowHelper.MESSAGE));

        final String outputStream2 = OutputReplacer.ERROR.run(new OutputReplacer.Function() {
            @Override
            public void run() {
                Json.prettyPrint(new ThrowHelper());
            }
        });
        assertTrue(outputStream2.contains(ThrowHelper.MESSAGE));
    }

    static class ThrowHelper {

        public static final String MESSAGE = "Test exception";

        public String getValue() throws IOException {
            throw new IOException(MESSAGE);
        }

        public void setValue(String value) {

        }
    }
}
