package io.swagger.v3.core.converting;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.oas.models.Person;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.JsonAssert;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.OutputReplacer;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class SwaggerSerializerTest {

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
                .schema("Person", personModel)
                .schema("Error", errorModel);

        final Operation get = new Operation()
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
                        .addMediaType("application/json", new MediaType()
                                .schema(new Schema().$ref("Person"))
                                .example("fun")));

        final ApiResponse errorResponse = new ApiResponse()
                .description("error response")
                .addLink("myLink", new Link()
                        .description("a link")
                        .operationId("theLinkedOperationId")
                        .addParameter("userId", "gah")
                )
                .content(new Content()
                        .addMediaType("application/json", new MediaType()
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
                        .addApiResponse("default", errorResponse))
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
                .schema("Person", personModel);

        final QueryParameter parameter = (QueryParameter) new QueryParameter()
                .name("id")
                .description("a common get parameter")
                .schema(new IntegerSchema());

        final Operation get = new Operation()
                .summary("finds pets in the system")
                .description("a longer description")
                .operationId("get pet by id")
                .addParametersItem(new Parameter().$ref("#/parameters/Foo"));

        swagger
                .components(swagger.getComponents().addParameters("Foo", parameter))
                .path("/pets", new PathItem().get(get));

        final String swaggerJson = Json.mapper().writeValueAsString(swagger);
        final OpenAPI rebuilt = Json.mapper().readValue(swaggerJson, OpenAPI.class);
        JsonAssert.assertJsonEquals(Json.mapper(), Json.pretty(rebuilt), Json.pretty(swagger));
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

    @Test
    public void testDynamicRefSerialization() throws IOException {
        Schema<?> dynamicRefSchema = new Schema<>();
        dynamicRefSchema.set$dynamicRef("#node");

        Components components = new Components().addSchemas("DynamicRefSchema", dynamicRefSchema);
        OpenAPI openAPI = new OpenAPI().components(components);
        String json = Json.mapper().writeValueAsString(openAPI);

        assertTrue(json.contains("\"$dynamicRef\":\"#node\""));
    }

    @Test
    public void test$defsSerializationV31Only() throws IOException {
        Schema<?> schema = new Schema<>().add$defs("dataType",
                new Schema<>().$dynamicAnchor("dataType").$ref("#/components/schemas/Pet"));

        Components components = new Components().addSchemas("Response", schema);
        OpenAPI openAPI = new OpenAPI().components(components);

        String json31 = Json31.mapper().writeValueAsString(openAPI);
        assertTrue(json31.contains("\"$defs\""));
        assertTrue(json31.contains("\"dataType\""));

        String json30 = Json.mapper().writeValueAsString(openAPI);
        assertFalse(json30.contains("\"$defs\""));
    }

    @Test
    public void test$defsExtensionsCoexistence() throws IOException {
        // Legacy producer workaround: $defs injected into extensions map, $defs field is null.
        // Expected: still emits $defs once, sourced from extensions. No regression for producers
        // that haven't migrated to set$defs().
        Schema<?> legacyOnly = new Schema<>();
        Map<String, Object> legacyExt = new HashMap<>();
        legacyExt.put("$defs", java.util.Collections.singletonMap("legacy", "value"));
        legacyOnly.setExtensions(legacyExt);
        String legacyJson = Json31.mapper().writeValueAsString(legacyOnly);
        assertEquals(countOccurrences(legacyJson, "\"$defs\""), 1,
                "legacy workaround (extensions only) should emit $defs exactly once");
        assertTrue(legacyJson.contains("\"legacy\""));

        // New path: $defs field only. Expected: clean single emission.
        Schema<?> fieldOnly = new Schema<>();
        fieldOnly.add$defs("k", new Schema<>());
        String fieldJson = Json31.mapper().writeValueAsString(fieldOnly);
        assertEquals(countOccurrences(fieldJson, "\"$defs\""), 1);

        // Mixed (both field and extensions populated): documented edge case.
        // Jackson emits both — duplicate $defs keys. This is not a regression (impossible before
        // the field existed) but producers MUST NOT mix the two paths.
        Schema<?> mixed = new Schema<>();
        mixed.add$defs("fromField", new Schema<>());
        Map<String, Object> mixedExt = new HashMap<>();
        mixedExt.put("$defs", java.util.Collections.singletonMap("fromExt", "v"));
        mixed.setExtensions(mixedExt);
        String mixedJson = Json31.mapper().writeValueAsString(mixed);
        assertEquals(countOccurrences(mixedJson, "\"$defs\""), 2,
                "mixing field and extensions produces duplicate $defs keys (documented, not a bug)");
    }

    private static int countOccurrences(String haystack, String needle) {
        int count = 0, idx = 0;
        while ((idx = haystack.indexOf(needle, idx)) != -1) {
            count++;
            idx += needle.length();
        }
        return count;
    }

}
