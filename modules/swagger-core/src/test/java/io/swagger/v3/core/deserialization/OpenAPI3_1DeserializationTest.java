package io.swagger.v3.core.deserialization;

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.LinkedHashMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNull;

public class OpenAPI3_1DeserializationTest {

    @Test
    public void deserializePetstore3_1() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1.yaml");
        final OpenAPI swagger = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
    }

    @Test
    public void deserializePetstore3_1More() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1_more.yaml");
        final OpenAPI swagger = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
    }


    @Test
    public void deserializePetstore3_0() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/petstore-3.0.yaml");
        final OpenAPI swagger = Yaml.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertNull(swagger.getInfo().getLicense().getIdentifier());
    }

    @Test
    public void deserializeChangelog3_1() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/changelog-3.1.yaml");
        final OpenAPI swagger = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
        Yaml31.prettyPrint(swagger);
        SerializationMatchers.assertEqualsToYaml31(swagger, jsonString);
    }

    @Test
    public void testDeserializationOnOAS31() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1_sample.yaml");
        OpenAPI openAPI = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(openAPI);

        assertEquals(openAPI.getInfo().getTitle(), "Swagger Petstore");
        assertEquals(openAPI.getInfo().getVersion(), "1.0.0");
        assertEquals(openAPI.getInfo().getSummary(), "petstore sample for OAS 3.1.0");
        assertEquals(openAPI.getInfo().getLicense().getName(), "MIT");
        assertEquals(openAPI.getInfo().getLicense().getIdentifier(), "test");

        assertNotNull(openAPI.getWebhooks());
        assertFalse(openAPI.getWebhooks().isEmpty());
        assertNotNull(openAPI.getWebhooks().get("newPet"));
        assertNotNull(openAPI.getWebhooks().get("newPet").getPost());

        assertNotNull(openAPI.getComponents().getPathItems());
        assertNotNull(openAPI.getComponents().getPathItems().get("/pet"));
        assertEquals(openAPI.getComponents().getPathItems().get("/pet").getDescription(), "get a pet");
        assertNotNull(openAPI.getComponents().getPathItems().get("/pet").getGet());
        assertEquals(openAPI.getComponents().getPathItems().get("/pet").getGet().getOperationId(), "getPet");

        assertNotNull(openAPI.getComponents().getSchemas());
        assertNotNull(openAPI.getComponents().getSchemas().get("Pet"));
        assertNotNull(openAPI.getComponents().getSchemas().get("Pet").getDiscriminator());
        assertNotNull(openAPI.getComponents().getSchemas().get("Pet").getDiscriminator().getExtensions());
        assertEquals(openAPI.getComponents().getSchemas().get("Pet").getDiscriminator().getExtensions().get("x-test-extension"), "extended");

        assertNotNull(openAPI.getComponents().getResponses());
        assertNotNull(openAPI.getComponents().getResponses().get("201"));
        assertEquals(openAPI.getComponents().getResponses().get("201").getDescription(), "api response description");

        assertNotNull(openAPI.getComponents().getParameters());
        assertNotNull(openAPI.getComponents().getParameters().get("param"));
        assertEquals(openAPI.getComponents().getParameters().get("param").getIn(), "query");
        assertEquals(openAPI.getComponents().getParameters().get("param").getName(), "param0");
        assertEquals(openAPI.getComponents().getParameters().get("param").getDescription(), "parameter description");

        assertNotNull(openAPI.getComponents().getExamples());
        assertNotNull(openAPI.getComponents().getExamples().get("example"));
        assertEquals(openAPI.getComponents().getExamples().get("example").getDescription(), "example description");
        assertEquals(openAPI.getComponents().getExamples().get("example").getSummary(), "example summary");
        assertEquals(openAPI.getComponents().getExamples().get("example").getValue(), "This is an example");

        assertNotNull(openAPI.getComponents().getRequestBodies());
        assertNotNull(openAPI.getComponents().getRequestBodies().get("body"));

        assertNotNull(openAPI.getComponents().getHeaders());
        assertNotNull(openAPI.getComponents().getHeaders().get("test-head"));
        assertEquals(openAPI.getComponents().getHeaders().get("test-head").getDescription(), "test header description");

        assertNotNull(openAPI.getComponents().getSecuritySchemes());
        assertNotNull(openAPI.getComponents().getSecuritySchemes().get("basic"));
        assertEquals(openAPI.getComponents().getSecuritySchemes().get("basic").getDescription(), "security description");
        assertEquals(openAPI.getComponents().getSecuritySchemes().get("basic").getType().toString(), "http");

        assertNotNull(openAPI.getComponents().getLinks());
        assertNotNull(openAPI.getComponents().getLinks().get("Link"));
        assertEquals(openAPI.getComponents().getLinks().get("Link").getOperationRef(), "#/paths/~12.0~1repositories~1{username}/get");

        assertNotNull(openAPI.getComponents().getCallbacks());
        assertNotNull(openAPI.getComponents().getCallbacks().get("TestCallback"));

        openAPI = Yaml.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(openAPI);

        assertNull(openAPI.getInfo().getSummary());
        assertNull(openAPI.getWebhooks());
        assertNull(openAPI.getComponents().getPathItems());
        assertNull(openAPI.getComponents().getSchemas().get("Pet").getDiscriminator().getExtensions());

    }

    @Test
    public void testDeserializationOnOAS30() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1_sample.yaml");
        OpenAPI openAPI = Yaml.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(openAPI);

        assertEquals(openAPI.getInfo().getTitle(), "Swagger Petstore");
        assertEquals(openAPI.getInfo().getVersion(), "1.0.0");
        assertNull(openAPI.getInfo().getSummary());
        assertEquals(openAPI.getInfo().getLicense().getName(), "MIT");
        assertNull(openAPI.getInfo().getLicense().getIdentifier());

        assertNull(openAPI.getWebhooks());

        assertNull(openAPI.getComponents().getPathItems());

        assertNotNull(openAPI.getComponents().getSchemas());
        assertNotNull(openAPI.getComponents().getSchemas().get("Pet"));
        assertNotNull(openAPI.getComponents().getSchemas().get("Pet").getDiscriminator());
        assertNull(openAPI.getComponents().getSchemas().get("Pet").getDiscriminator().getExtensions());

        assertNotNull(openAPI.getComponents().getResponses());
        assertNotNull(openAPI.getComponents().getResponses().get("201"));
        assertEquals(openAPI.getComponents().getResponses().get("201").getDescription(), "api response description");

        assertNotNull(openAPI.getComponents().getParameters());
        assertNotNull(openAPI.getComponents().getParameters().get("param"));
        assertEquals(openAPI.getComponents().getParameters().get("param").getIn(), "query");
        assertEquals(openAPI.getComponents().getParameters().get("param").getName(), "param0");
        assertEquals(openAPI.getComponents().getParameters().get("param").getDescription(), "parameter description");

        assertNotNull(openAPI.getComponents().getExamples());
        assertNotNull(openAPI.getComponents().getExamples().get("example"));
        assertEquals(openAPI.getComponents().getExamples().get("example").getDescription(), "example description");
        assertEquals(openAPI.getComponents().getExamples().get("example").getSummary(), "example summary");
        assertEquals(openAPI.getComponents().getExamples().get("example").getValue(), "This is an example");

        assertNotNull(openAPI.getComponents().getRequestBodies());
        assertNotNull(openAPI.getComponents().getRequestBodies().get("body"));

        assertNotNull(openAPI.getComponents().getHeaders());
        assertNotNull(openAPI.getComponents().getHeaders().get("test-head"));
        assertEquals(openAPI.getComponents().getHeaders().get("test-head").getDescription(), "test header description");

        assertNotNull(openAPI.getComponents().getSecuritySchemes());
        assertNotNull(openAPI.getComponents().getSecuritySchemes().get("basic"));
        assertEquals(openAPI.getComponents().getSecuritySchemes().get("basic").getDescription(), "security description");
        assertEquals(openAPI.getComponents().getSecuritySchemes().get("basic").getType().toString(), "http");

        assertNotNull(openAPI.getComponents().getLinks());
        assertNotNull(openAPI.getComponents().getLinks().get("Link"));
        assertEquals(openAPI.getComponents().getLinks().get("Link").getOperationRef(), "#/paths/~12.0~1repositories~1{username}/get");

        assertNotNull(openAPI.getComponents().getCallbacks());
        assertNotNull(openAPI.getComponents().getCallbacks().get("TestCallback"));

        openAPI = Yaml.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(openAPI);
    }

    @Test
    public void testRefDeserializationOnOAS31() throws IOException {
        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1_refs_siblings.yaml");
        OpenAPI openAPI = Yaml31.mapper().readValue(jsonString, OpenAPI.class);


        assertEquals(openAPI.getPaths().get("/ref_pet").get$ref(), "#/components/pathItems/pet");
        assertEquals(openAPI.getPaths().get("/ref_pet").getDescription(), "ref pathItem description");
        assertEquals(openAPI.getPaths().get("/ref_pet").getSummary(), "ref pathItem summary");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(0).get$ref(), "#/components/parameters/testParameter");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(0).getDescription(), "ref parameter description");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(1).getName(), "randomParam");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(1).getIn(), "query");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(1).getExamples().get("refExample").get$ref(), "#/components/examples/testExample");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(1).getExamples().get("refExample").getDescription(), "ref example description");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getParameters().get(1).getExamples().get("refExample").getSummary(), "ref example summary");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getCallbacks().get("callIt").get$ref(), "#/components/callbacks/TestCallback");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getRequestBody().get$ref(), "#/components/requestBodies/body");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getRequestBody().getDescription(), "ref request body description");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getResponses().get("201").get$ref(), "#/components/responses/okResponse");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getResponses().get("201").getDescription(), "ref response description");

        assertEquals(openAPI.getPaths().get("/pets").getPost().getResponses().get("default").getHeaders().get("head").get$ref(), "#/components/headers/head");
        assertEquals(openAPI.getPaths().get("/pets").getPost().getResponses().get("default").getHeaders().get("head").getDescription(), "ref header description");

    }

    @Test
    public void testBooleanSchemaDeserialization() throws Exception{
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .components(new Components().addSchemas("test", new Schema().booleanSchemaValue(true)));

        String json = Json31.pretty(openAPI);
        String yaml = Yaml31.pretty(openAPI);
        OpenAPI oas = Json31.mapper().readValue(json, OpenAPI.class);
        assertEquals(oas.getComponents().getSchemas().get("test").getBooleanSchemaValue(), Boolean.TRUE);
        Schema schema = Json31.mapper().readValue("true", Schema.class);
        assertEquals(schema.getBooleanSchemaValue(), Boolean.TRUE);
        oas = Yaml31.mapper().readValue(yaml, OpenAPI.class);
        assertEquals(oas.getComponents().getSchemas().get("test").getBooleanSchemaValue(), Boolean.TRUE);
        schema = Yaml31.mapper().readValue("true", Schema.class);
        assertEquals(schema.getBooleanSchemaValue(), Boolean.TRUE);

        json = Json.pretty(openAPI);
        yaml = Yaml.pretty(openAPI);
        oas = Json.mapper().readValue(json, OpenAPI.class);
        assertNull(oas.getComponents().getSchemas().get("test").getBooleanSchemaValue());
        oas = Yaml.mapper().readValue(yaml, OpenAPI.class);
        assertNull(oas.getComponents().getSchemas().get("test").getBooleanSchemaValue());
    }

    @Test
    public void testDynamicRefDeserializationOnOAS31() throws IOException {
        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/specWithDynamicRef.yaml");
        OpenAPI openAPI = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(openAPI);

        Schema baseNodeSchema = openAPI.getComponents().getSchemas().get("BaseNode");
        assertNotNull(baseNodeSchema);
        assertNotNull(baseNodeSchema.getProperties().get("children"));
        JsonSchema childrenSchema = (JsonSchema) baseNodeSchema.getProperties().get("children");
        assertEquals(childrenSchema.getItems().get$dynamicRef(), "#node");
    }

    @Test
    public void test$defsRoundTripOnOAS31() throws IOException {
        Schema<?> dataTypeSlot = new Schema<>()
                .$dynamicAnchor("dataType")
                .not(new Schema<>());
        Schema<?> template = new Schema<>()
                .$dynamicAnchor("dataType")
                .addProperty("data", new Schema<>().$dynamicRef("#dataType"))
                .$defs(new LinkedHashMap<>());
        template.get$defs().put("dataType", dataTypeSlot);

        Schema<?> binding = new Schema<>()
                .$ref("#/components/schemas/Response")
                .add$defs("dataType", new Schema<>().$dynamicAnchor("dataType").$ref("#/components/schemas/Pet"));

        Components components = new Components()
                .addSchemas("Response", template)
                .addSchemas("Pet", new Schema().type("object").addProperty("id", new Schema().type("integer")));
        OpenAPI openAPI = new OpenAPI().openapi("3.1.0").components(components);
        openAPI.path("/pet", new io.swagger.v3.oas.models.PathItem()
                .get(new io.swagger.v3.oas.models.Operation()
                        .responses(new io.swagger.v3.oas.models.responses.ApiResponses()
                                .addApiResponse("200", new io.swagger.v3.oas.models.responses.ApiResponse()
                                        .content(new io.swagger.v3.oas.models.media.Content()
                                                .addMediaType("application/json", new io.swagger.v3.oas.models.media.MediaType()
                                                        .schema(binding)))))));

        String json = Json31.pretty(openAPI);
        OpenAPI parsed = Json31.mapper().readValue(json, OpenAPI.class);
        String jsonAgain = Json31.pretty(parsed);

        Schema<?> parsedTemplate = parsed.getComponents().getSchemas().get("Response");
        assertNotNull(parsedTemplate.get$defs(), "template $defs should round-trip");
        assertNotNull(parsedTemplate.get$defs().get("dataType"), "template $defs dataType slot should survive");
        assertEquals(parsedTemplate.get$defs().get("dataType").get$dynamicAnchor(), "dataType");

        Schema<?> parsedBinding = parsed.getPaths().get("/pet").getGet().getResponses().get("200")
                .getContent().get("application/json").getSchema();
        assertNotNull(parsedBinding.get$defs(), "inline binding $defs should round-trip");
        assertEquals(parsedBinding.get$defs().get("dataType").get$ref(), "#/components/schemas/Pet");
        assertEquals(parsedBinding.get$ref(), "#/components/schemas/Response");

        SerializationMatchers.assertEqualsToJson31(parsed, jsonAgain);
    }

    @Test
    public void test$defsAccumulationAndEquality() {
        Schema<?> a = new Schema<>();
        assertNull(a.get$defs());
        a.add$defs("k1", new Schema().type("string"));
        a.add$defs("k2", new Schema().type("integer"));
        assertEquals(a.get$defs().size(), 2);

        Schema<?> b = new Schema<>();
        b.add$defs("k1", new Schema().type("string"));
        b.add$defs("k2", new Schema().type("integer"));
        assertEquals(a, b);

        Schema<?> c = new Schema<>();
        c.add$defs("k1", new Schema().type("string"));
        assertNotEquals(a, c);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void test$defsRoutedToFieldNotExtensions() throws IOException {
        // Behavioral change: $defs in input JSON is now routed to the first-class field,
        // not the extensions map. Consumers reading $defs from extensions must migrate to get$defs().
        String json = "{\"$defs\":{\"k\":{\"type\":\"string\"}}}";
        Schema<?> parsed = Json31.mapper().readValue(json, Schema.class);

        assertNotNull(parsed.get$defs(), "$defs should land in the dedicated field");
        assertNotNull(parsed.get$defs().get("k"), "$defs entry should be parsed as a Schema");
        assertNull(parsed.getExtensions(),
                "$defs must NOT also appear in extensions (would cause duplicate emission on re-serialize)");
    }

}
