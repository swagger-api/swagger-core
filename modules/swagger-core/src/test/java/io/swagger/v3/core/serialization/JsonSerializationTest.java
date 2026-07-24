package io.swagger.v3.core.serialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.dataformat.yaml.JacksonYAMLParseException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.ObjectMapperFactory;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.LoaderOptions;

import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class JsonSerializationTest {

    @Test
    public void testSerializeASpecWithPathReferences() throws Exception {

        OpenAPI swagger = new OpenAPI()
                .addServersItem(new Server().url("http://petstore.swagger.io"));

        PathItem expectedPath = new PathItem().$ref("http://my.company.com/paths/health.json");
        swagger.path("/health", expectedPath);

        String swaggerJson = Json.mapper().writeValueAsString(swagger);
        OpenAPI rebuilt = Json.mapper().readValue(swaggerJson, OpenAPI.class);

        final PathItem path = rebuilt.getPaths().get("/health");
        assertEquals(path, expectedPath);
    }

    @Test
    public void testQueryHttpMethodIsNotSerializedBeforeOpenAPI32() throws Exception {

        Operation queryOperation = new Operation()
                .operationId("searchPets")
                .responses(new ApiResponses()
                        .addApiResponse("200", new ApiResponse().description("ok")));

        PathItem pathItem = new PathItem()
                .get(new Operation().operationId("listPets"))
                .query(queryOperation);
        OpenAPI openAPI = new OpenAPI().path("/pets", pathItem);

        // the model holds the operation ...
        assertNotNull(openAPI.getPaths().get("/pets").getQuery());

        // ... but "query" is a Path Item fixed field only as of OpenAPI 3.2, so neither the
        // 3.0 nor the 3.1 mapper may emit it, while the other methods serialize as usual
        for (String json : new String[]{
                Json.mapper().writeValueAsString(openAPI),
                Json31.mapper().writeValueAsString(openAPI)}) {
            assertFalse(json.contains("\"query\""));
            assertTrue(json.contains("\"listPets\""));
            assertFalse(json.contains("\"searchPets\""));
        }
    }

    @Test
    public void testQueryHttpMethodIsNotDeserializedBeforeOpenAPI32() throws Exception {

        String json = "{\"openapi\":\"3.0.1\",\"paths\":{\"/pets\":{"
                + "\"get\":{\"operationId\":\"listPets\"},"
                + "\"query\":{\"operationId\":\"searchPets\"}}}}";

        PathItem pathItem = Json.mapper().readValue(json, OpenAPI.class).getPaths().get("/pets");

        assertNotNull(pathItem.getGet());
        assertNull(pathItem.getQuery());
    }

    @Test
    public void testExtension() throws Exception {

        OpenAPI swagger = new OpenAPI();
        swagger.addExtension("x-foo-bar", "foo bar");
        swagger.setInfo(new Info());
        swagger.getInfo().addExtension("x-foo-bar", "foo bar");

        String swaggerJson = Json.mapper().writeValueAsString(swagger);
        assertFalse(swaggerJson.contains("extensions"));
        OpenAPI rebuilt = Json.mapper().readValue(swaggerJson, OpenAPI.class);
        assertEquals(rebuilt.getExtensions().values().iterator().next(), "foo bar");
        assertEquals(rebuilt.getInfo().getExtensions().values().iterator().next(), "foo bar");

    }

    @Test
    public void testExtensionObjectWithProperties() throws Exception {
        final Map<String, Object> extensionObjectProps = new HashMap<>();
        extensionObjectProps.put("x-foo-bar", "foo bar");
        extensionObjectProps.put("x-bar-foo", null);

        OpenAPI swagger = new OpenAPI();
        swagger.addExtension("x-extension-with-properties", extensionObjectProps);

        String swaggerJson = Json.mapper().writeValueAsString(swagger);
        assertEquals(swaggerJson, "{\"openapi\":\"3.0.1\",\"x-extension-with-properties\":{\"x-foo-bar\":\"foo bar\",\"x-bar-foo\":null}}");
    }

    @Test
    public void testSerializeASpecWithResponseReferences() throws Exception {
        OpenAPI swagger = new OpenAPI()
                .addServersItem(new Server().url("http://petstore.swagger.io"));

        ApiResponse expectedResponse = new ApiResponse().$ref("http://my.company.com/paths/health.json");
        PathItem expectedPath = new PathItem()
                .get(
                        new Operation().responses(
                                new ApiResponses()
                                        .addApiResponse("200", expectedResponse)));

        swagger.path("/health", expectedPath);

        String swaggerJson = Json.mapper().writeValueAsString(swagger);
        OpenAPI rebuilt = Json.mapper().readValue(swaggerJson, OpenAPI.class);

        assertEquals(rebuilt.getPaths().get("/health").getGet().getResponses().get("200"), expectedResponse);

    }

    @Test
    public void testSerializeNullExample() throws Exception {
        final String yaml = ResourceUtils.loadClassResource(getClass(), "specFiles/null-example.yaml");
        OpenAPI deser = Yaml.mapper().readValue(yaml, OpenAPI.class);
        SerializationMatchers.assertEqualsToYaml(deser, yaml);

    }

    @Test
    public void testSerializeNullInSchemaExample() throws Exception {
        final String yaml = ResourceUtils.loadClassResource(getClass(), "specFiles/null-in-schema-example.yaml");
        OpenAPI deser = Yaml.mapper().readValue(yaml, OpenAPI.class);
        SerializationMatchers.assertEqualsToYaml(deser, yaml);

    }

    @Test
    public void testSerializeJSONWithCustomFactory() throws Exception {
        // given
        JsonFactory jsonFactory = new JsonFactory();
        final String json = ResourceUtils.loadClassResource(getClass(), "specFiles/petstore-3.0.json");
        final String expectedJson = ResourceUtils.loadClassResource(getClass(), "specFiles/jsonSerialization-expected-petstore-3.0.json");

        // when
        OpenAPI deser = ObjectMapperFactory.createJson(jsonFactory).readValue(json, OpenAPI.class);

        // then
        SerializationMatchers.assertEqualsToJson(deser, expectedJson);
    }

    @Test
    public void testSerializeYAMLWithCustomFactory() throws Exception {
        // given
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setCodePointLimit(5 * 1024 * 1024);
        YAMLFactory yamlFactory = YAMLFactory.builder()
                .loaderOptions(loaderOptions)
                .build();
        final String yaml = ResourceUtils.loadClassResource(getClass(), "specFiles/null-example.yaml");

        // when
        OpenAPI deser = ObjectMapperFactory.createYaml(yamlFactory).readValue(yaml, OpenAPI.class);

        // then
        SerializationMatchers.assertEqualsToYaml(deser, yaml);
    }

    @Test(expectedExceptions = JacksonYAMLParseException.class)
    public void testSerializeYAMLWithCustomFactoryAndCodePointLimitReached() throws Exception {
        // given
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setCodePointLimit(1);
        YAMLFactory yamlFactory = YAMLFactory.builder()
                .loaderOptions(loaderOptions)
                .build();
        final String yaml = ResourceUtils.loadClassResource(getClass(), "specFiles/null-example.yaml");

        // when
        OpenAPI deser = ObjectMapperFactory.createYaml(yamlFactory).readValue(yaml, OpenAPI.class);

        // then - Throw JacksonYAMLParseException
    }
}
