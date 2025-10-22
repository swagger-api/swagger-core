package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.testng.Assert.assertTrue;

public final class JsonAssert {
    private JsonAssert() {
    }

    /**
     * Asserts that two JSON strings are semantically equal, ignoring key order.
     * This version uses a default failure message.
     *
     * @param mapper       The ObjectMapper to use for parsing.
     * @param expectedJson The expected JSON/YAML string.
     * @param actualJson   The actual JSON/YAML string.
    */
    public static void assertJsonEquals(ObjectMapper mapper, String expectedJson, String actualJson) {
        assertJsonEquals(mapper, expectedJson, actualJson, "The JSON structures are not equal.");
    }

    /**
     * This version allows for a custom failure message.
     *
     * @param mapper       The ObjectMapper to use for parsing.
     * @param expectedJson The expected JSON/YAML string.
     * @param actualJson   The actual JSON/YAML string.
     * @param message      The custom message to display if the assertion fails.
    */
    public static void assertJsonEquals(ObjectMapper mapper, String expectedJson, String actualJson, String message) {
        try {
            JsonNode expectedNode = mapper.readTree(expectedJson);
            JsonNode actualNode = mapper.readTree(actualJson);
            assertTrue(expectedNode.equals(actualNode), message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}