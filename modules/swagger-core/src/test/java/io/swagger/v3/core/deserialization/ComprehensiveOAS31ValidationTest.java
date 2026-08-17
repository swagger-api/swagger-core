package io.swagger.v3.core.deserialization;

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Test class to validate the comprehensive OpenAPI 3.1 files.
 */
public class ComprehensiveOAS31ValidationTest {

    /**
     * Test to validate the comprehensive OpenAPI 3.1 file.
     * This test ensures that the file can be properly deserialized by the swagger-core library
     * and that all references are correctly resolved.
     */
    @Test
    public void testComprehensiveOAS31Validation() throws IOException {
        // Path to the comprehensive OpenAPI 3.1 file
        String filePath = "src/test/resources/comprehensiveOAS31/comprehensive-openapi.yaml";

        // Verify the file exists
        File file = new File(filePath);
        assertTrue(file.exists(), "Comprehensive OpenAPI 3.1 file does not exist: " + filePath);

        // Deserialize the file
        OpenAPI openAPI = Yaml31.mapper().readValue(file, OpenAPI.class);

        // Verify the file was properly deserialized
        assertNotNull(openAPI, "Failed to deserialize the comprehensive OpenAPI 3.1 file");

        // Verify basic structure
        assertNotNull(openAPI.getInfo(), "Info object is missing");
        assertNotNull(openAPI.getPaths(), "Paths object is missing");
        assertNotNull(openAPI.getComponents(), "Components object is missing");

        // Verify components
        assertNotNull(openAPI.getComponents().getSchemas(), "Schemas object is missing");
        assertNotNull(openAPI.getComponents().getPathItems(), "PathItems object is missing");
        assertNotNull(openAPI.getComponents().getParameters(), "Parameters object is missing");
        assertNotNull(openAPI.getComponents().getRequestBodies(), "RequestBodies object is missing");
        assertNotNull(openAPI.getComponents().getResponses(), "Responses object is missing");
        assertNotNull(openAPI.getComponents().getHeaders(), "Headers object is missing");
        assertNotNull(openAPI.getComponents().getSecuritySchemes(), "SecuritySchemes object is missing");
        assertNotNull(openAPI.getComponents().getExamples(), "Examples object is missing");
        assertNotNull(openAPI.getComponents().getLinks(), "Links object is missing");
        assertNotNull(openAPI.getComponents().getCallbacks(), "Callbacks object is missing");

        // Verify webhooks
        assertNotNull(openAPI.getWebhooks(), "Webhooks object is missing");

        // Verify schemas
        assertTrue(openAPI.getComponents().getSchemas().size() > 0, "No schemas found");

        // Verify paths
        assertTrue(openAPI.getPaths().size() > 0, "No paths found");

        // Verify webhooks
        assertTrue(openAPI.getWebhooks().size() > 0, "No webhooks found");
    }

    /**
     * Test round-trip serialization and deserialization of the comprehensive OpenAPI 3.1 file using YAML.
     * This test ensures that the file can be properly serialized and deserialized without losing information.
     */
    @Test
    public void testComprehensiveOAS31YamlRoundTrip() throws IOException {
        // Path to the comprehensive OpenAPI 3.1 file
        String filePath = "src/test/resources/comprehensiveOAS31/comprehensive-openapi.yaml";

        // Verify the file exists
        File file = new File(filePath);
        assertTrue(file.exists(), "Comprehensive OpenAPI 3.1 file does not exist: " + filePath);

        // Deserialize the file
        OpenAPI originalOpenAPI = Yaml31.mapper().readValue(file, OpenAPI.class);

        // Serialize to YAML
        String yaml = Yaml31.pretty(originalOpenAPI);

        // Deserialize back
        OpenAPI deserializedOpenAPI = Yaml31.mapper().readValue(yaml, OpenAPI.class);

        // Verify basic structure
        assertNotNull(deserializedOpenAPI, "Failed to deserialize the serialized YAML");
        assertEquals(deserializedOpenAPI.getOpenapi(), "3.1.0", "OpenAPI version mismatch");
        assertEquals(deserializedOpenAPI.getInfo().getTitle(), originalOpenAPI.getInfo().getTitle(), "Title mismatch");
        assertEquals(deserializedOpenAPI.getInfo().getVersion(), originalOpenAPI.getInfo().getVersion(), "Version mismatch");

        // Verify components
        assertNotNull(deserializedOpenAPI.getComponents(), "Components object is missing");
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas(), "Schemas object is missing");
        assertEquals(deserializedOpenAPI.getComponents().getSchemas().size(), originalOpenAPI.getComponents().getSchemas().size(), "Schemas size mismatch");

        // Verify paths
        assertNotNull(deserializedOpenAPI.getPaths(), "Paths object is missing");
        assertEquals(deserializedOpenAPI.getPaths().size(), originalOpenAPI.getPaths().size(), "Paths size mismatch");

        // Verify webhooks
        assertNotNull(deserializedOpenAPI.getWebhooks(), "Webhooks object is missing");
        assertEquals(deserializedOpenAPI.getWebhooks().size(), originalOpenAPI.getWebhooks().size(), "Webhooks size mismatch");

        // Serialize again
        String yamlAgain = Yaml31.pretty(deserializedOpenAPI);

        // Compare YAML strings (normalize whitespace)
        SerializationMatchers.assertEqualsToYaml31(originalOpenAPI, yamlAgain);
    }

    /**
     * Test round-trip serialization and deserialization of the comprehensive OpenAPI 3.1 file using JSON.
     * This test ensures that the file can be properly serialized and deserialized without losing information.
     */
    @Test
    public void testComprehensiveOAS31JsonRoundTrip() throws IOException {
        // Path to the comprehensive OpenAPI 3.1 file
        String filePath = "src/test/resources/comprehensiveOAS31/comprehensive-openapi.yaml";

        // Verify the file exists
        File file = new File(filePath);
        assertTrue(file.exists(), "Comprehensive OpenAPI 3.1 file does not exist: " + filePath);

        // Deserialize the file
        OpenAPI originalOpenAPI = Yaml31.mapper().readValue(file, OpenAPI.class);

        // Serialize to JSON
        String json = Json31.pretty(originalOpenAPI);

        // Deserialize back
        OpenAPI deserializedOpenAPI = Json31.mapper().readValue(json, OpenAPI.class);

        // Verify basic structure
        assertNotNull(deserializedOpenAPI, "Failed to deserialize the serialized JSON");
        assertEquals(deserializedOpenAPI.getOpenapi(), "3.1.0", "OpenAPI version mismatch");
        assertEquals(deserializedOpenAPI.getInfo().getTitle(), originalOpenAPI.getInfo().getTitle(), "Title mismatch");
        assertEquals(deserializedOpenAPI.getInfo().getVersion(), originalOpenAPI.getInfo().getVersion(), "Version mismatch");

        // Verify components
        assertNotNull(deserializedOpenAPI.getComponents(), "Components object is missing");
        assertNotNull(deserializedOpenAPI.getComponents().getSchemas(), "Schemas object is missing");
        assertEquals(deserializedOpenAPI.getComponents().getSchemas().size(), originalOpenAPI.getComponents().getSchemas().size(), "Schemas size mismatch");

        // Verify paths
        assertNotNull(deserializedOpenAPI.getPaths(), "Paths object is missing");
        assertEquals(deserializedOpenAPI.getPaths().size(), originalOpenAPI.getPaths().size(), "Paths size mismatch");

        // Verify webhooks
        assertNotNull(deserializedOpenAPI.getWebhooks(), "Webhooks object is missing");
        assertEquals(deserializedOpenAPI.getWebhooks().size(), originalOpenAPI.getWebhooks().size(), "Webhooks size mismatch");

        // Serialize again
        String jsonAgain = Json31.pretty(deserializedOpenAPI);

        // Compare JSON strings
        SerializationMatchers.assertEqualsToJson31(originalOpenAPI, jsonAgain);
    }
}
