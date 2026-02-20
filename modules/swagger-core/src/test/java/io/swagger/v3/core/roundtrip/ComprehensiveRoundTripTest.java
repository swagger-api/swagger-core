package io.swagger.v3.core.roundtrip;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.JsonAssert;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Comprehensive tests for round-trip (serialization + deserialization) of OpenAPI 3.0 and 3.1 documents.
 * This test class covers various scenarios, edge cases, and combinations of OpenAPI elements.
 */
public class ComprehensiveRoundTripTest {

    /**
     * Test round-trip of OpenAPI 3.0 document with JSON
     */
    @Test
    public void testRoundTrip30Json() throws IOException {
        // Create a simple OpenAPI 3.0 document
        OpenAPI originalOpenAPI = createBasicOpenAPI30();
        
        // Serialize to JSON
        String json = Json.pretty(originalOpenAPI);
        
        // Deserialize back
        OpenAPI deserializedOpenAPI = Json.mapper().readValue(json, OpenAPI.class);
        
        // Verify basic structure
        assertNotNull(deserializedOpenAPI);
        assertEquals(deserializedOpenAPI.getOpenapi(), "3.0.1");
        assertEquals(deserializedOpenAPI.getInfo().getTitle(), "Test API");
        assertEquals(deserializedOpenAPI.getInfo().getVersion(), "1.0.0");
        
        // Verify paths
        assertNotNull(deserializedOpenAPI.getPaths());
        assertTrue(deserializedOpenAPI.getPaths().containsKey("/test"));
        
        // Verify components
        assertNotNull(deserializedOpenAPI.getComponents());
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas());
        assertTrue(deserializedOpenAPI.getComponents().getSchemas().containsKey("TestSchema"));
        
        // Serialize again
        String jsonAgain = Json.pretty(deserializedOpenAPI);
        
        // Compare JSON strings
        JsonAssert.assertJsonEquals(Json.mapper(), json, jsonAgain, "JSON round-trip failed");
    }
    
    /**
     * Test round-trip of OpenAPI 3.0 document with YAML
     */
    @Test
    public void testRoundTrip30Yaml() throws IOException {
        // Create a simple OpenAPI 3.0 document
        OpenAPI originalOpenAPI = createBasicOpenAPI30();
        
        // Serialize to YAML
        String yaml = Yaml.pretty(originalOpenAPI);
        
        // Deserialize back
        OpenAPI deserializedOpenAPI = Yaml.mapper().readValue(yaml, OpenAPI.class);
        
        // Verify basic structure
        assertNotNull(deserializedOpenAPI);
        assertEquals(deserializedOpenAPI.getOpenapi(), "3.0.1");
        assertEquals(deserializedOpenAPI.getInfo().getTitle(), "Test API");
        assertEquals(deserializedOpenAPI.getInfo().getVersion(), "1.0.0");
        
        // Verify paths
        assertNotNull(deserializedOpenAPI.getPaths());
        assertTrue(deserializedOpenAPI.getPaths().containsKey("/test"));
        
        // Verify components
        assertNotNull(deserializedOpenAPI.getComponents());
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas());
        assertTrue(deserializedOpenAPI.getComponents().getSchemas().containsKey("TestSchema"));
        
        // Serialize again
        String yamlAgain = Yaml.pretty(deserializedOpenAPI);
        
        // Compare YAML strings
        JsonAssert.assertJsonEquals(Yaml.mapper(), yaml, yamlAgain, "YAML round-trip failed");
    }
    
    /**
     * Test round-trip of OpenAPI 3.1 document with JSON
     */
    @Test
    public void testRoundTrip31Json() throws IOException {
        // Create a simple OpenAPI 3.1 document
        OpenAPI originalOpenAPI = createBasicOpenAPI31();
        
        // Serialize to JSON
        String json = Json31.pretty(originalOpenAPI);
        
        // Deserialize back
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);
        
        // Verify basic structure
        assertNotNull(deserializedOpenAPI);
        assertEquals(deserializedOpenAPI.getOpenapi(), "3.1.0");
        assertEquals(deserializedOpenAPI.getInfo().getTitle(), "Test API");
        assertEquals(deserializedOpenAPI.getInfo().getVersion(), "1.0.0");
        assertEquals(deserializedOpenAPI.getInfo().getSummary(), "Test API Summary");
        assertEquals(deserializedOpenAPI.getInfo().getLicense().getIdentifier(), "MIT");
        
        // Verify paths
        assertNotNull(deserializedOpenAPI.getPaths());
        assertTrue(deserializedOpenAPI.getPaths().containsKey("/test"));
        
        // Verify components
        assertNotNull(deserializedOpenAPI.getComponents());
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas());
        assertTrue(deserializedOpenAPI.getComponents().getSchemas().containsKey("TestSchema"));
        
        // Verify webhooks
        assertNotNull(deserializedOpenAPI.getWebhooks());
        assertTrue(deserializedOpenAPI.getWebhooks().containsKey("testWebhook"));
        
        // Serialize again
        String jsonAgain = Json31.pretty(deserializedOpenAPI);
        
        // Compare JSON strings
        JsonAssert.assertJsonEquals(Json31.mapper(), json, jsonAgain, "JSON round-trip failed");
    }
    
    /**
     * Test round-trip of OpenAPI 3.1 document with YAML
     */
    @Test
    public void testRoundTrip31Yaml() throws IOException {
        // Create a simple OpenAPI 3.1 document
        OpenAPI originalOpenAPI = createBasicOpenAPI31();
        
        // Serialize to YAML
        String yaml = Yaml31.pretty(originalOpenAPI);
        
        // Deserialize back
        OpenAPI deserializedOpenAPI = Yaml31.mapper().readValue(yaml, OpenAPI.class);
        
        // Verify basic structure
        assertNotNull(deserializedOpenAPI);
        assertEquals(deserializedOpenAPI.getOpenapi(), "3.1.0");
        assertEquals(deserializedOpenAPI.getInfo().getTitle(), "Test API");
        assertEquals(deserializedOpenAPI.getInfo().getVersion(), "1.0.0");
        assertEquals(deserializedOpenAPI.getInfo().getSummary(), "Test API Summary");
        assertEquals(deserializedOpenAPI.getInfo().getLicense().getIdentifier(), "MIT");
        
        // Verify paths
        assertNotNull(deserializedOpenAPI.getPaths());
        assertTrue(deserializedOpenAPI.getPaths().containsKey("/test"));
        
        // Verify components
        assertNotNull(deserializedOpenAPI.getComponents());
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas());
        assertTrue(deserializedOpenAPI.getComponents().getSchemas().containsKey("TestSchema"));
        
        // Verify webhooks
        assertNotNull(deserializedOpenAPI.getWebhooks());
        assertTrue(deserializedOpenAPI.getWebhooks().containsKey("testWebhook"));
        
        // Serialize again
        String yamlAgain = Yaml31.pretty(deserializedOpenAPI);
        
        // Compare YAML strings
        JsonAssert.assertJsonEquals(Yaml31.mapper(), yaml, yamlAgain, "YAML round-trip failed");
    }
    
    /**
     * Test round-trip of OpenAPI 3.1 document with complex structures
     */
    @Test
    public void testComplexRoundTrip31() throws IOException {
        // Create an OpenAPI 3.1 document with complex structures
        OpenAPI originalOpenAPI = createComplexOpenAPI31();
        
        // Serialize to JSON
        String json = Json31.pretty(originalOpenAPI);
        
        // Deserialize back
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);
        
        // Verify complex structures
        
        // Verify discriminator extensions
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema").getDiscriminator());
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema").getDiscriminator().getExtensions());
        assertEquals(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema").getDiscriminator().getExtensions().get("x-test-extension"), "test-value");
        
        // Verify component path items
        assertNotNull(deserializedOpenAPI.getComponents().getPathItems());
        assertTrue(deserializedOpenAPI.getComponents().getPathItems().containsKey("/testPathItem"));
        assertEquals(deserializedOpenAPI.getComponents().getPathItems().get("/testPathItem").getDescription(), "Test path item");
        assertEquals(deserializedOpenAPI.getComponents().getPathItems().get("/testPathItem").getSummary(), "Test path item summary");
        assertNotNull(deserializedOpenAPI.getComponents().getPathItems().get("/testPathItem").getGet());
        assertEquals(deserializedOpenAPI.getComponents().getPathItems().get("/testPathItem").getGet().getOperationId(), "getTest");
        
        // Verify path item refs and siblings
        assertEquals(deserializedOpenAPI.getPaths().get("/refTest").get$ref(), "#/components/pathItems/testPathItem");
        assertEquals(deserializedOpenAPI.getPaths().get("/refTest").getDescription(), "Ref path item description");
        assertEquals(deserializedOpenAPI.getPaths().get("/refTest").getSummary(), "Ref path item summary");
        
        // Verify parameter refs and siblings
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(0).get$ref(), "#/components/parameters/testParameter");
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(0).getDescription(), "Ref parameter description");
        
        // Verify example refs and siblings
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(1).getName(), "testParam");
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(1).getIn(), "query");
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(1).getExamples().get("refExample").get$ref(), "#/components/examples/testExample");
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(1).getExamples().get("refExample").getSummary(), "Ref example summary");
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(1).getExamples().get("refExample").getDescription(), "Ref example description");
        
        // Serialize again
        String jsonAgain = Json31.pretty(deserializedOpenAPI);
        
        // Compare JSON strings
        JsonAssert.assertJsonEquals(Json31.mapper(), json, jsonAgain, "JSON round-trip failed");
    }
    
    /**
     * Test round-trip of OpenAPI 3.1 document with boolean schema
     */
    @Test
    public void testBooleanSchemaRoundTrip() throws IOException {
        // Create an OpenAPI 3.1 document with boolean schema
        OpenAPI originalOpenAPI = createBasicOpenAPI31();
        
        // Add boolean schema
        Schema booleanSchema = new Schema().booleanSchemaValue(true);
        originalOpenAPI.getComponents().getSchemas().put("BooleanSchema", booleanSchema);
        
        // Serialize to JSON
        String json = Json31.pretty(originalOpenAPI);
        
        // Deserialize back
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);
        
        // Verify boolean schema
        assertTrue(Boolean.TRUE.equals(deserializedOpenAPI.getComponents().getSchemas().get("BooleanSchema").getBooleanSchemaValue()));
        
        // Verify that the schema is serialized as a boolean value
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        assertTrue(jsonNode.get("components").get("schemas").get("BooleanSchema").isBoolean());
        assertTrue(jsonNode.get("components").get("schemas").get("BooleanSchema").asBoolean());
        
        // Serialize again
        String jsonAgain = Json31.pretty(deserializedOpenAPI);
        
        // Compare JSON strings
        JsonAssert.assertJsonEquals(Json.mapper(), json, jsonAgain, "JSON round-trip failed");
    }
    
    /**
     * Test round-trip of OpenAPI 3.1 document with null values
     */
    @Test
    public void testNullValuesRoundTrip() throws IOException {
        // Create an OpenAPI 3.1 document with null values
        OpenAPI originalOpenAPI = createBasicOpenAPI31();
        
        // Add schema with null example
        Schema schema = originalOpenAPI.getComponents().getSchemas().get("TestSchema");
        schema.setExample(null);
        schema.setExampleSetFlag(true);
        
        // Serialize to JSON
        String json = Json31.pretty(originalOpenAPI);
        
        // Deserialize back
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);
        
        // Verify null values
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema"));
        assertEquals(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema").getExample(), null);
        assertTrue(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema").getExampleSetFlag());
        
        // Serialize again
        String jsonAgain = Json31.pretty(deserializedOpenAPI);
        
        // Compare JSON strings
        JsonAssert.assertJsonEquals(Json.mapper(), json, jsonAgain, "JSON round-trip failed");
    }
    
    /**
     * Test round-trip of real-world OpenAPI 3.1 document
     */
    @Test
    public void testRealWorldRoundTrip31() throws IOException {
        // Load a real-world OpenAPI 3.1 document
        final String yaml = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1_sample.yaml");
        final OpenAPI originalOpenAPI = Yaml31.mapper().readValue(yaml, OpenAPI.class);
        
        // Serialize to YAML
        String serializedYaml = Yaml31.pretty(originalOpenAPI);
        
        // Deserialize back
        OpenAPI deserializedOpenAPI = Yaml31.mapper().readValue(serializedYaml, OpenAPI.class);
        
        // Verify basic structure
        assertNotNull(deserializedOpenAPI);
        assertEquals(deserializedOpenAPI.getOpenapi(), "3.1.0");
        assertEquals(deserializedOpenAPI.getInfo().getTitle(), "Swagger Petstore");
        assertEquals(deserializedOpenAPI.getInfo().getVersion(), "1.0.0");
        assertEquals(deserializedOpenAPI.getInfo().getSummary(), "petstore sample for OAS 3.1.0");
        assertEquals(deserializedOpenAPI.getInfo().getLicense().getName(), "MIT");
        assertEquals(deserializedOpenAPI.getInfo().getLicense().getIdentifier(), "test");
        
        // Verify webhooks
        assertNotNull(deserializedOpenAPI.getWebhooks());
        assertNotNull(deserializedOpenAPI.getWebhooks().get("newPet"));
        assertNotNull(deserializedOpenAPI.getWebhooks().get("newPet").getPost());
        
        // Verify component path items
        assertNotNull(deserializedOpenAPI.getComponents().getPathItems());
        assertNotNull(deserializedOpenAPI.getComponents().getPathItems().get("/pet"));
        assertEquals(deserializedOpenAPI.getComponents().getPathItems().get("/pet").getDescription(), "get a pet");
        assertNotNull(deserializedOpenAPI.getComponents().getPathItems().get("/pet").getGet());
        assertEquals(deserializedOpenAPI.getComponents().getPathItems().get("/pet").getGet().getOperationId(), "getPet");
        
        // Serialize again
        String yamlAgain = Yaml31.pretty(deserializedOpenAPI);
        
        // Compare YAML strings (normalize whitespace)
        JsonAssert.assertJsonEquals(Yaml31.mapper(), serializedYaml, yamlAgain, "YAML round-trip failed");
    }
    
    /**
     * Helper method to create a basic OpenAPI 3.0 document
     */
    private OpenAPI createBasicOpenAPI30() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setOpenapi("3.0.1");
        
        // Add info
        Info info = new Info()
                .title("Test API")
                .version("1.0.0")
                .description("Test API Description")
                .contact(new Contact().name("Test Contact").email("test@example.com"))
                .license(new License().name("MIT"));
        openAPI.setInfo(info);
        
        // Add paths
        Paths paths = new Paths();
        PathItem pathItem = new PathItem();
        
        // Add GET operation
        Operation getOperation = new Operation()
                .operationId("getTest")
                .summary("Get Test")
                .description("Get Test Description");
        
        // Add responses
        ApiResponses responses = new ApiResponses();
        ApiResponse response = new ApiResponse()
                .description("Successful response");
        responses.addApiResponse("200", response);
        getOperation.setResponses(responses);
        
        pathItem.setGet(getOperation);
        paths.addPathItem("/test", pathItem);
        openAPI.setPaths(paths);
        
        // Add components
        Components components = new Components();
        Schema schema = new Schema()
                .type("object")
                .title("TestSchema")
                .description("Test Schema Description");
        components.addSchemas("TestSchema", schema);
        openAPI.setComponents(components);
        
        return openAPI;
    }
    
    /**
     * Helper method to create a basic OpenAPI 3.1 document
     */
    private OpenAPI createBasicOpenAPI31() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setOpenapi("3.1.0");
        
        // Add info
        Info info = new Info()
                .title("Test API")
                .version("1.0.0")
                .description("Test API Description")
                .summary("Test API Summary")
                .contact(new Contact().name("Test Contact").email("test@example.com"))
                .license(new License().name("MIT").identifier("MIT"));
        openAPI.setInfo(info);
        
        // Add paths
        Paths paths = new Paths();
        PathItem pathItem = new PathItem();
        
        // Add GET operation
        Operation getOperation = new Operation()
                .operationId("getTest")
                .summary("Get Test")
                .description("Get Test Description");
        
        // Add responses
        ApiResponses responses = new ApiResponses();
        ApiResponse response = new ApiResponse()
                .description("Successful response");
        responses.addApiResponse("200", response);
        getOperation.setResponses(responses);
        
        pathItem.setGet(getOperation);
        paths.addPathItem("/test", pathItem);
        openAPI.setPaths(paths);
        
        // Add components
        Components components = new Components();
        Schema schema = new Schema()
                .type("object")
                .title("TestSchema")
                .description("Test Schema Description");
        components.addSchemas("TestSchema", schema);
        openAPI.setComponents(components);
        
        // Add webhooks
        Map<String, PathItem> webhooks = new HashMap<>();
        PathItem webhookPathItem = new PathItem();
        Operation postOperation = new Operation()
                .operationId("postWebhook")
                .summary("Post Webhook")
                .description("Post Webhook Description");
        webhookPathItem.setPost(postOperation);
        webhooks.put("testWebhook", webhookPathItem);
        openAPI.setWebhooks(webhooks);
        
        return openAPI;
    }
    
    /**
     * Helper method to create a complex OpenAPI 3.1 document
     */
    private OpenAPI createComplexOpenAPI31() {
        OpenAPI openAPI = createBasicOpenAPI31();
        
        // Add discriminator with extensions
        Schema schema = openAPI.getComponents().getSchemas().get("TestSchema");
        Discriminator discriminator = new Discriminator().propertyName("type");
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("x-test-extension", "test-value");
        discriminator.setExtensions(extensions);
        schema.setDiscriminator(discriminator);
        
        // Add component path items
        PathItem pathItem = new PathItem()
                .description("Test path item")
                .summary("Test path item summary");
        pathItem.setGet(new Operation().operationId("getTest"));
        
        openAPI.getComponents().pathItems(new HashMap<>());
        openAPI.getComponents().getPathItems().put("/testPathItem", pathItem);
        
        // Add path with reference and siblings
        PathItem refPathItem = new PathItem()
                .$ref("#/components/pathItems/testPathItem")
                .description("Ref path item description")
                .summary("Ref path item summary");
        openAPI.getPaths().put("/refTest", refPathItem);
        
        // Add component parameters
        Parameter parameter = new Parameter()
                .name("testParam")
                .in("query")
                .description("Test parameter");
        
        openAPI.getComponents().parameters(new HashMap<>());
        openAPI.getComponents().getParameters().put("testParameter", parameter);
        
        // Add operation with parameter reference and siblings
        Parameter refParameter = new Parameter()
                .$ref("#/components/parameters/testParameter")
                .description("Ref parameter description");
        
        openAPI.getPaths().get("/test").getGet().addParametersItem(refParameter);
        
        // Add component examples
        Example example = new Example()
                .summary("Test example summary")
                .description("Test example description")
                .value("Test example value");
        
        openAPI.getComponents().examples(new HashMap<>());
        openAPI.getComponents().getExamples().put("testExample", example);
        
        // Add parameter with example reference and siblings
        Parameter paramWithExample = new Parameter()
                .name("testParam")
                .in("query")
                .description("Test parameter");
        
        Example refExample = new Example()
                .$ref("#/components/examples/testExample")
                .summary("Ref example summary")
                .description("Ref example description");
        
        paramWithExample.examples(new HashMap<>());
        paramWithExample.getExamples().put("refExample", refExample);
        
        openAPI.getPaths().get("/test").getGet().addParametersItem(paramWithExample);
        
        return openAPI;
    }
}