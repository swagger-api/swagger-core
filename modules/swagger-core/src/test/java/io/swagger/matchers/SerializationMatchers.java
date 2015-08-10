package io.swagger.matchers;

import static org.testng.Assert.fail;

import io.swagger.util.Json;
import io.swagger.util.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SerializationMatchers {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializationMatchers.class);

    public static void assertEqualsToYaml(Object objectToSerialize, String yamlStr) {
        apply(objectToSerialize, yamlStr, Yaml.mapper());
    }

    public static void assertEqualsToJson(Object objectToSerialize, String jsonStr) {
        apply(objectToSerialize, jsonStr, Json.mapper());
    }

    private static void apply(Object objectToSerialize, String str, ObjectMapper mapper) {
        final ObjectNode lhs = mapper.convertValue(objectToSerialize, ObjectNode.class);
        ObjectNode rhs = null;
        try {
            rhs = mapper.readValue(str, ObjectNode.class);
        } catch (IOException e) {
            LOGGER.error("Failed to read value", e);
        }
        if (!lhs.equals(rhs)) {
            fail(String.format("Serialized object:\n%s\ndoes not equal to expected serialized string:\n%s", lhs, rhs));
        }
    }
}
