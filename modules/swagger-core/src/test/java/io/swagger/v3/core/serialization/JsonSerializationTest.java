package io.swagger.v3.core.serialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.dataformat.yaml.JacksonYAMLParseException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Json;
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

    @Test
    public void testCustomPrettyPrinterIsHonored() {
        //given
        DefaultPrettyPrinter originalPrinter = (DefaultPrettyPrinter) Json.mapper().getSerializationConfig().getDefaultPrettyPrinter();
        
        try {
            DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
            printer.indentObjectsWith(new DefaultIndenter("    ", "\n"));
            Json.mapper().setDefaultPrettyPrinter(printer);

            //when
            OpenAPI openAPI = new OpenAPI()
                    .info(new Info().title("Pet Store"));

            String json = Json.pretty(openAPI);

            //then
            assertTrue(json.contains("{\n    \"openapi\""),
                    "Custom four-space indentation should be honored");
            assertTrue(json.contains("    \"info\" : {"), 
                    "Nested objects should use four-space indentation");
            assertTrue(json.contains("        \"title\" : \"Pet Store\""), 
                    "Doubly-nested properties should use eight-space indentation");
        } finally {
            // Restore original pretty printer to avoid affecting other tests
            Json.mapper().setDefaultPrettyPrinter(originalPrinter);
        }
    }
}
