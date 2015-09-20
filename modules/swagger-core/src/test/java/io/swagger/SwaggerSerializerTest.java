package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Contact;
import io.swagger.models.Error;
import io.swagger.models.Info;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Person;
import io.swagger.models.RefModel;
import io.swagger.models.Response;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.parameters.RefParameter;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;
import io.swagger.util.OutputReplacer;
import io.swagger.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SwaggerSerializerTest {
    ObjectMapper m = Json.mapper();

    @Test(description = "it should convert a spec")
    public void convertSpec() throws IOException {
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

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "value");
        info.setVendorExtension("x-test2", map);
        info.setVendorExtension("x-test", "value");

        final Swagger swagger = new Swagger()
                .info(info)
                .host("petstore.swagger.io")
                .securityDefinition("api-key", new ApiKeyAuthDefinition("key", In.HEADER))
                .scheme(Scheme.HTTP)
                .consumes("application/json")
                .produces("application/json")
                .model("Person", personModel)
                .model("Error", errorModel);

        final Operation get = new Operation()
                .produces("application/json")
                .summary("finds pets in the system")
                .description("a longer description")
                .tag("Pet Operations")
                .operationId("get pet by id")
                .deprecated(true);

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
                .schema(new RefProperty().asDefault("Person"))
                .example("application/json", "fun!");

        final Response errorResponse = new Response()
                .description("error response")
                .schema(new RefProperty().asDefault("Error"));

        get.response(200, response)
                .defaultResponse(errorResponse);

        final Operation post = new Operation()
                .summary("adds a new pet")
                .description("you can add a new pet this way")
                .tag("Pet Operations")
                .operationId("add pet")
                .defaultResponse(errorResponse)
                .parameter(new BodyParameter()
                        .description("the pet to add")
                        .schema(new RefModel().asDefault("Person")));

        swagger.path("/pets", new Path().get(get).post(post));
        final String swaggerJson = Json.mapper().writeValueAsString(swagger);
        final Swagger rebuilt = Json.mapper().readValue(swaggerJson, Swagger.class);
        SerializationMatchers.assertEqualsToJson(rebuilt, swaggerJson);
    }

    @Test(description = "it should read the uber api")
    public void readUberApi() throws IOException {
        final String jsonString = ResourceUtils.loadClassResource(getClass(), "uber.json");
        final Swagger swagger = Json.mapper().readValue(jsonString, Swagger.class);
        assertNotNull(swagger);
    }

    @Test(description = "it should write a spec with parameter references")
    public void writeSpecWithParameterReferences() throws IOException {
        final Model personModel = ModelConverters.getInstance().read(Person.class).get("Person");

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
                .securityDefinition("api-key", new ApiKeyAuthDefinition("key", In.HEADER))
                .scheme(Scheme.HTTP)
                .consumes("application/json")
                .produces("application/json")
                .model("Person", personModel);

        final QueryParameter parameter = new QueryParameter()
                .name("id")
                .description("a common get parameter")
                .property(new LongProperty());

        final Operation get = new Operation()
                .produces("application/json")
                .summary("finds pets in the system")
                .description("a longer description")
                .tag("Pet Operations")
                .operationId("get pet by id")
                .parameter(new RefParameter("foo"));

        swagger.parameter("foo", parameter)
                .path("/pets", new Path().get(get));

        final String swaggerJson = Json.mapper().writeValueAsString(swagger);
        final Swagger rebuilt = Json.mapper().readValue(swaggerJson, Swagger.class);
        assertEquals(Json.pretty(swagger), Json.pretty(rebuilt));
    }

    @Test
    public void prettyPrintTest() throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), "uber.json");
        final Swagger swagger = Json.mapper().readValue(json, Swagger.class);
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
