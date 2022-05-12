package io.swagger.v3.core.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;

import static org.testng.Assert.assertEquals;

public class SerializationMatchers {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerializationMatchers.class);

    public static void assertEqualsToYaml(Object objectToSerialize, String yamlStr) {
        apply(objectToSerialize, yamlStr, Yaml.mapper());
    }

    public static void assertEqualsToJson(Object objectToSerialize, String jsonStr) {
        apply(objectToSerialize, jsonStr, Json.mapper());
    }

    public static void assertEqualsToYaml31(Object objectToSerialize, String yamlStr) {
        apply31(objectToSerialize, yamlStr, Yaml31.mapper());
    }

    public static void assertEqualsToJson31(Object objectToSerialize, String jsonStr) {
        apply31(objectToSerialize, jsonStr, Json31.mapper());
    }

    private static void apply(Object objectToSerialize, String str, ObjectMapper mapper) {
        final ObjectNode lhs = mapper.convertValue(objectToSerialize, ObjectNode.class);
        ObjectNode rhs = null;
        try {
            rhs = mapper.readValue(str, ObjectNode.class);
        } catch (IOException e) {
            LOGGER.error("Failed to read value", e);
        }
        if (!lhs.equals(new ObjectNodeComparator(), rhs)) {
            assertEquals(Yaml.pretty(lhs), Yaml.pretty(rhs));
        }
    }

    private static void apply31(Object objectToSerialize, String str, ObjectMapper mapper) {
        final ObjectNode lhs = mapper.convertValue(objectToSerialize, ObjectNode.class);
        ObjectNode rhs = null;
        try {
            rhs = mapper.readValue(str, ObjectNode.class);
        } catch (IOException e) {
            LOGGER.error("Failed to read value", e);
        }
        if (!lhs.equals(new ObjectNodeComparator(), rhs)) {
            assertEquals(Yaml31.pretty(lhs), Yaml31.pretty(rhs));
        }
    }

    static final class ObjectNodeComparator implements Comparator<JsonNode> {
        @Override
        public int compare(JsonNode o1, JsonNode o2) {
            if (o1.equals(o2)) {
                return 0;
            }
            if ((o1 instanceof NumericNode) && (o2 instanceof NumericNode)) {
                double d1 = ((NumericNode) o1).asDouble();
                double d2 = ((NumericNode) o2).asDouble();
                return Double.compare(d1, d2);
            }
            int comp = o1.asText().compareTo(o2.asText());
            if (comp == 0) {
                return Integer.compare(o1.hashCode(), o2.hashCode());
            }
            return comp;
        }
    }
}
