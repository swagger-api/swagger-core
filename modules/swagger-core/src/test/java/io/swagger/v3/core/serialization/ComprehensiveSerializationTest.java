package io.swagger.v3.core.serialization;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.examples.Example;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Comprehensive tests for serialization of OpenAPI 3.0 and 3.1 documents.
 * This test class covers various scenarios, edge cases, and combinations of OpenAPI elements.
 */
public class ComprehensiveSerializationTest {

    /**
     * Test basic serialization of OpenAPI 3.0 document to JSON
     */
    @Test
    public void testBasicSerialization30Json() throws IOException {
        // Create a simple OpenAPI 3.0 document
        OpenAPI openAPI = createBasicOpenAPI30();
        
        // Serialize to JSON
        String json = Json.pretty(openAPI);
        
        // Deserialize back to verify
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
    }
    
    /**
     * Test basic serialization of OpenAPI 3.0 document to YAML
     */
    @Test
    public void testBasicSerialization30Yaml() throws IOException {
        // Create a simple OpenAPI 3.0 document
        OpenAPI openAPI = createBasicOpenAPI30();
        
        // Serialize to YAML
        String yaml = Yaml.pretty(openAPI);
        
        // Deserialize back to verify
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
    }
    
    /**
     * Test basic serialization of OpenAPI 3.1 document to JSON
     */
    @Test
    public void testBasicSerialization31Json() throws IOException {
        // Create a simple OpenAPI 3.1 document
        OpenAPI openAPI = createBasicOpenAPI31();
        
        // Serialize to JSON
        String json = Json31.pretty(openAPI);
        
        // Deserialize back to verify
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
    }
    
    /**
     * Test basic serialization of OpenAPI 3.1 document to YAML
     */
    @Test
    public void testBasicSerialization31Yaml() throws IOException {
        // Create a simple OpenAPI 3.1 document
        OpenAPI openAPI = createBasicOpenAPI31();
        
        // Serialize to YAML
        String yaml = Yaml31.pretty(openAPI);
        
        // Deserialize back to verify
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
    }
    
    /**
     * Test serialization of OpenAPI 3.1 document with component path items
     */
    @Test
    public void testComponentPathItemsSerialization() throws IOException {
        // Create an OpenAPI 3.1 document with component path items
        OpenAPI openAPI = createBasicOpenAPI31();
        
        // Add component path items
        PathItem pathItem = new PathItem()
                .description("Test path item")
                .summary("Test path item summary");
        pathItem.setGet(new Operation().operationId("getTest"));
        
        openAPI.getComponents().pathItems(new HashMap<>());
        openAPI.getComponents().getPathItems().put("/testPathItem", pathItem);
        
        // Serialize to JSON
        String json = Json31.pretty(openAPI);
        
        // Deserialize back to verify
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);
        
        // Verify component path items
        assertNotNull(deserializedOpenAPI.getComponents().getPathItems());
        assertTrue(deserializedOpenAPI.getComponents().getPathItems().containsKey("/testPathItem"));
        assertEquals(deserializedOpenAPI.getComponents().getPathItems().get("/testPathItem").getDescription(), "Test path item");
        assertEquals(deserializedOpenAPI.getComponents().getPathItems().get("/testPathItem").getSummary(), "Test path item summary");
        assertNotNull(deserializedOpenAPI.getComponents().getPathItems().get("/testPathItem").getGet());
        assertEquals(deserializedOpenAPI.getComponents().getPathItems().get("/testPathItem").getGet().getOperationId(), "getTest");
    }
    
    /**
     * Test serialization of OpenAPI 3.1 document with discriminator extensions
     */
    @Test
    public void testDiscriminatorExtensionsSerialization() throws IOException {
        // Create an OpenAPI 3.1 document with discriminator extensions
        OpenAPI openAPI = createBasicOpenAPI31();
        
        // Add discriminator with extensions
        Schema schema = openAPI.getComponents().getSchemas().get("TestSchema");
        Discriminator discriminator = new Discriminator().propertyName("type");
        Map<String, Object> extensions = new HashMap<>();
        extensions.put("x-test-extension", "test-value");
        discriminator.setExtensions(extensions);
        schema.setDiscriminator(discriminator);
        
        // Serialize to JSON
        String json = Json31.pretty(openAPI);
        
        // Deserialize back to verify
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);
        
        // Verify discriminator extensions
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema").getDiscriminator());
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema").getDiscriminator().getExtensions());
        assertEquals(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema").getDiscriminator().getExtensions().get("x-test-extension"), "test-value");
    }
    
    /**
     * Test serialization of OpenAPI 3.1 document with path item references and siblings
     */
    @Test
    public void testPathItemRefsAndSiblingsSerialization() throws IOException {
        // Create an OpenAPI 3.1 document with path item references and siblings
        OpenAPI openAPI = createBasicOpenAPI31();
        
        // Add component path items
        PathItem pathItem = new PathItem()
                .description("Test path item")
                .summary("Test path item summary");
        pathItem.setGet(new Operation().operationId("getTest"));
        
        openAPI.getComponents().pathItems(new HashMap<>());
        openAPI.getComponents().getPathItems().put("testPathItem", pathItem);
        
        // Add path with reference and siblings
        PathItem refPathItem = new PathItem()
                .$ref("#/components/pathItems/testPathItem")
                .description("Ref path item description")
                .summary("Ref path item summary");
        openAPI.getPaths().put("/refTest", refPathItem);
        
        // Serialize to JSON
        String json = Json31.pretty(openAPI);
        
        // Deserialize back to verify
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);
        
        // Verify path item refs and siblings
        assertEquals(deserializedOpenAPI.getPaths().get("/refTest").get$ref(), "#/components/pathItems/testPathItem");
        assertEquals(deserializedOpenAPI.getPaths().get("/refTest").getDescription(), "Ref path item description");
        assertEquals(deserializedOpenAPI.getPaths().get("/refTest").getSummary(), "Ref path item summary");
    }
    
    /**
     * Test serialization of OpenAPI 3.1 document with parameter references and siblings
     */
    @Test
    public void testParameterRefsAndSiblingsSerialization() throws IOException {
        // Create an OpenAPI 3.1 document with parameter references and siblings
        OpenAPI openAPI = createBasicOpenAPI31();
        
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
        
        // Serialize to JSON
        String json = Json31.pretty(openAPI);
        
        // Deserialize back to verify
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);
        
        // Verify parameter refs and siblings
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(0).get$ref(), "#/components/parameters/testParameter");
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(0).getDescription(), "Ref parameter description");
    }
    
    /**
     * Test serialization of OpenAPI 3.1 document with example references and siblings
     */
    @Test
    public void testExampleRefsAndSiblingsSerialization() throws IOException {
        // Create an OpenAPI 3.1 document with example references and siblings
        OpenAPI openAPI = createBasicOpenAPI31();
        
        // Add component examples
        Example example = new Example()
                .summary("Test example summary")
                .description("Test example description")
                .value("Test example value");
        
        openAPI.getComponents().examples(new HashMap<>());
        openAPI.getComponents().getExamples().put("testExample", example);
        
        // Add parameter with example reference and siblings
        Parameter parameter = new Parameter()
                .name("testParam")
                .in("query")
                .description("Test parameter");
        
        Example refExample = new Example()
                .$ref("#/components/examples/testExample")
                .summary("Ref example summary")
                .description("Ref example description");
        
        parameter.examples(new HashMap<>());
        parameter.getExamples().put("refExample", refExample);
        
        openAPI.getPaths().get("/test").getGet().addParametersItem(parameter);
        
        // Serialize to JSON
        String json = Json31.pretty(openAPI);
        
        // Deserialize back to verify
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);
        
        // Verify example refs and siblings
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(0).getName(), "testParam");
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(0).getIn(), "query");
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(0).getExamples().get("refExample").get$ref(), "#/components/examples/testExample");
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(0).getExamples().get("refExample").getSummary(), "Ref example summary");
        assertEquals(deserializedOpenAPI.getPaths().get("/test").getGet().getParameters().get(0).getExamples().get("refExample").getDescription(), "Ref example description");
    }
    
    /**
     * Test serialization of OpenAPI 3.1 document with boolean schema
     */
    @Test
    public void testBooleanSchemaSerialization() throws IOException {
        // Create an OpenAPI 3.1 document with boolean schema
        OpenAPI openAPI = createBasicOpenAPI31();
        
        // Add boolean schema
        Schema booleanSchema = new Schema().booleanSchemaValue(true);
        openAPI.getComponents().getSchemas().put("BooleanSchema", booleanSchema);
        
        // Serialize to JSON
        String json = Json31.pretty(openAPI);
        
        // Deserialize back to verify
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);
        
        // Verify boolean schema
        assertTrue(Boolean.TRUE.equals(deserializedOpenAPI.getComponents().getSchemas().get("BooleanSchema").getBooleanSchemaValue()));
        
        // Verify that the schema is serialized as a boolean value
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        assertTrue(jsonNode.get("components").get("schemas").get("BooleanSchema").isBoolean());
        assertTrue(jsonNode.get("components").get("schemas").get("BooleanSchema").asBoolean());
    }
    
    /**
     * Test serialization of OpenAPI 3.1 document with null values
     */
    @Test
    public void testNullValuesSerialization() throws IOException {
        // Create an OpenAPI 3.1 document with null values
        OpenAPI openAPI = createBasicOpenAPI31();
        
        // Add schema with null example
        Schema schema = openAPI.getComponents().getSchemas().get("TestSchema");
        schema.setExample(null);
        schema.setExampleSetFlag(true);
        
        // Serialize to JSON
        String json = Json31.pretty(openAPI);
        
        // Deserialize back to verify
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);
        
        // Verify null values
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema"));
        assertEquals(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema").getExample(), null);
        assertTrue(deserializedOpenAPI.getComponents().getSchemas().get("TestSchema").getExampleSetFlag());
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
}