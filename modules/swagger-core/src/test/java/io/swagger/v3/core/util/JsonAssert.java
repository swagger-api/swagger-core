package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.testng.Assert.assertTrue;

public final class JsonAssert {
    private JsonAssert() {
    }

    public static void assertJsonEquals(ObjectMapper mapper, String expectedJson, String actualJson) {
        try {
            JsonNode expectedNode = mapper.readTree(expectedJson);
            JsonNode actualNode = mapper.readTree(actualJson);
            assertTrue(expectedNode.equals(actualNode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}