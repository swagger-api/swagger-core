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
 * test documenting how example values for numbers/non-number differ in their JSON-representation.
 */
public class Issue5061Test {


    @Test
    public void testExampleValuesAreSerializedAsJsonDifferentlyBetweenStringAndNumber() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(true).readAllAsResolvedSchema(
                ModelWithDifferentCombinationOfNumberFieldsWithExamples.class
        );

        assertNotNull(schema, "Schema should resolve");
        String json = Json31.pretty(schema);
        assertNotNull(json);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        JsonNode stringFieldType = root.at("/schema/properties/stringFieldType");
        assertExampleIsString(stringFieldType);

        JsonNode stringFieldTypeWithExplicitStringSchemaType = root.at("/schema/properties/stringFieldTypeWithExplicitStringSchemaType");
        assertExampleIsString(stringFieldTypeWithExplicitStringSchemaType);

        JsonNode stringFieldTypeWithExplicitNumberSchemaType = root.at("/schema/properties/stringFieldTypeWithExplicitNumberSchemaType");
        assertExampleIsNumber(stringFieldTypeWithExplicitNumberSchemaType);

        JsonNode stringFieldTypeWithExplicitIntegerSchemaType = root.at("/schema/properties/stringFieldTypeWithExplicitIntegerSchemaType");
        assertExampleIsNumber(stringFieldTypeWithExplicitIntegerSchemaType);

        JsonNode bigDecimalFieldTypeWithExplicitStringSchemaType = root.at("/schema/properties/bigDecimalFieldTypeWithExplicitStringSchemaType");
        assertExampleIsString(bigDecimalFieldTypeWithExplicitStringSchemaType);

        JsonNode bigDecimalFieldType = root.at("/schema/properties/bigDecimalFieldType");
        assertExampleIsNumber(bigDecimalFieldType);
    }

    private void assertExampleIsNumber(JsonNode node) {
        assertTrue(node.get("example").isNumber(), "should be a number");
    }

    private void assertExampleIsString(JsonNode node) {
        assertTrue(node.get("example").isTextual(), "should be a string");
    }

    public static class ModelWithDifferentCombinationOfNumberFieldsWithExamples {

        @io.swagger.v3.oas.annotations.media.Schema(example = "5 lacs per annum")
        String stringFieldType;

        @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "5 lacs per annum")
        String stringFieldTypeWithExplicitStringSchemaType;

        @io.swagger.v3.oas.annotations.media.Schema(type = "number", example = "10")
        String stringFieldTypeWithExplicitNumberSchemaType;

        @io.swagger.v3.oas.annotations.media.Schema(type = "integer", example = "5")
        String stringFieldTypeWithExplicitIntegerSchemaType;

        @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "13.37")
        BigDecimal bigDecimalFieldTypeWithExplicitStringSchemaType;

        @io.swagger.v3.oas.annotations.media.Schema(example = "13.37")
        BigDecimal bigDecimalFieldType;

        public String getStringFieldType() {
            return stringFieldType;
        }

        public String getStringFieldTypeWithExplicitStringSchemaType() {
            return stringFieldTypeWithExplicitStringSchemaType;
        }

        public String getStringFieldTypeWithExplicitNumberSchemaType() {
            return stringFieldTypeWithExplicitNumberSchemaType;
        }

        public String getStringFieldTypeWithExplicitIntegerSchemaType() {
            return stringFieldTypeWithExplicitIntegerSchemaType;
        }

        public BigDecimal getBigDecimalFieldTypeWithExplicitStringSchemaType() {
            return bigDecimalFieldTypeWithExplicitStringSchemaType;
        }

        public BigDecimal getBigDecimalFieldType() {
            return bigDecimalFieldType;
        }

    }
}
