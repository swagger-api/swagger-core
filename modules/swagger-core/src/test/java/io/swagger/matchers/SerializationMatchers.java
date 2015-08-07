package io.swagger.matchers;

import io.swagger.util.Json;
import io.swagger.util.Yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SerializationMatchers {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializationMatchers.class);

    public static boolean compareAsYaml(Object objectToSerialize, String yamlStr) {
        return apply(objectToSerialize, yamlStr, Yaml.mapper());
    }

    public static boolean compareAsJson(Object objectToSerialize, String jsonStr) {
        return apply(objectToSerialize, jsonStr, Json.mapper());
    }

    private static boolean apply(Object objectToSerialize, String str, ObjectMapper mapper) {
        final ObjectNode lhs = mapper.convertValue(objectToSerialize, ObjectNode.class);
        ObjectNode rhs = null;
        try {
            rhs = mapper.readValue(str, ObjectNode.class);
        } catch (IOException e) {
            LOGGER.error("Failed to read value", e);
        }
        return lhs.equals(rhs);
    }
}
