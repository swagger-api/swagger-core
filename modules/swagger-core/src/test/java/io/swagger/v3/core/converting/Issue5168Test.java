package io.swagger.v3.core.converting;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.Json31;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * test documenting how example values for booleans behave in their JSON-representation.
 */
public class Issue5168Test {

    @Test
    public void testExampleValuesAreSerializedAsJsonDifferentlyBetweenStringAndBoolean() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(true).readAllAsResolvedSchema(
                ModelWithDifferentCombinationOfBooleanFieldsWithExamples.class
        );

        assertNotNull(schema, "Schema should resolve");
        String json = Json31.pretty(schema);
        assertNotNull(json);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        JsonNode stringFieldType = root.at("/schema/properties/stringFieldType");
        assertExampleIsString(stringFieldType);

        JsonNode stringFieldTypeWithExplicitBooleanSchemaType = root.at("/schema/properties/stringFieldTypeWithExplicitBooleanSchemaType");
        assertExampleIsBoolean(stringFieldTypeWithExplicitBooleanSchemaType);

        JsonNode booleanFieldTypeWithExplicitStringSchemaType = root.at("/schema/properties/booleanFieldTypeWithExplicitStringSchemaType");
        assertExampleIsString(booleanFieldTypeWithExplicitStringSchemaType);

        JsonNode booleanFieldType = root.at("/schema/properties/booleanFieldType");
        assertExampleIsBoolean(booleanFieldType);
    }

    private void assertExampleIsBoolean(JsonNode node) {
        assertTrue(node.get("example").isBoolean(), "should be a boolean");
    }

    private void assertExampleIsString(JsonNode node) {
        assertTrue(node.get("example").isTextual(), "should be a string");
    }

    public static class ModelWithDifferentCombinationOfBooleanFieldsWithExamples {

        @io.swagger.v3.oas.annotations.media.Schema(example = "true")
        String stringFieldType;

        @io.swagger.v3.oas.annotations.media.Schema(type = "boolean", example = "true")
        String stringFieldTypeWithExplicitBooleanSchemaType;

        @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "true")
        boolean booleanFieldTypeWithExplicitStringSchemaType;

        @io.swagger.v3.oas.annotations.media.Schema(example = "true")
        boolean booleanFieldType;

        public String getStringFieldType() {
            return stringFieldType;
        }

        public String getStringFieldTypeWithExplicitBooleanSchemaType() {
            return stringFieldTypeWithExplicitBooleanSchemaType;
        }

        public boolean isBooleanFieldTypeWithExplicitStringSchemaType() {
            return booleanFieldTypeWithExplicitStringSchemaType;
        }

        public boolean getBooleanFieldType() {
            return booleanFieldType;
        }
    }
}
