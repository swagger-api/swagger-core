package io.swagger.v3.oas.models.media;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SchemaTest {

    @Test
    public void testEqualsWithDifferentBooleanSchemaValue() {
        Schema<Object> schema1 = new Schema<>();
        Schema<Object> schema2 = new Schema<>();

        schema1.setBooleanSchemaValue(true);
        schema2.setBooleanSchemaValue(false);

        assertFalse(schema1.equals(schema2));
        assertFalse(schema2.equals(schema1));
        assertNotEquals(schema1.hashCode(), schema2.hashCode());
    }


    @Test
    public void testEqualsWithOneNullBooleanSchemaValue() {
        Schema<Object> schema1 = new Schema<>();
        Schema<Object> schema2 = new Schema<>();

        schema1.setBooleanSchemaValue(true);
        schema2.setBooleanSchemaValue(null);

        assertFalse(schema1.equals(schema2));
        assertFalse(schema2.equals(schema1));
        assertNotEquals(schema1.hashCode(), schema2.hashCode());
    }

    @Test
    public void testBooleanSchemaValueFalseVsTrue() {
        // This test specifically addresses the issue where false gets mapped to true
        Schema<Object> schemaFalse = new Schema<>();
        Schema<Object> schemaTrue = new Schema<>();

        schemaFalse.setBooleanSchemaValue(false);
        schemaTrue.setBooleanSchemaValue(true);

        // These should be different
        assertFalse(schemaFalse.equals(schemaTrue));
        assertFalse(schemaTrue.equals(schemaFalse));

        // Hash codes should be different to prevent collisions in maps
        assertNotEquals(schemaFalse.hashCode(), schemaTrue.hashCode());

        // Verify the actual values
        assertEquals(Boolean.FALSE, schemaFalse.getBooleanSchemaValue());
        assertEquals(Boolean.TRUE, schemaTrue.getBooleanSchemaValue());
    }

    @Test
    public void testComplexSchemaEqualsWithBooleanSchemaValue() {
        Schema<Object> schema1 = createComplexSchema();
        Schema<Object> schema2 = createComplexSchema();

        // Initially they should be equal
        assertTrue(schema1.equals(schema2));
        assertEquals(schema1.hashCode(), schema2.hashCode());

        // Change only booleanSchemaValue
        schema2.setBooleanSchemaValue(false);

        // Now they should be different
        assertFalse(schema1.equals(schema2));
        assertNotEquals(schema1.hashCode(), schema2.hashCode());
    }


    @Test
    public void testBooleanSchemaValueInMapLookup() {
        Map<Schema<Object>, String> visitedMap = new HashMap<>();

        Schema<Object> schemaTrue = new Schema<>();
        Schema<Object> schemaFalse = new Schema<>();

        schemaTrue.setBooleanSchemaValue(true);
        schemaFalse.setBooleanSchemaValue(false);

        visitedMap.put(schemaTrue, "true_schema");
        visitedMap.put(schemaFalse, "false_schema");

        assertEquals(visitedMap.size(), 2);
        assertEquals(visitedMap.get(schemaTrue), "true_schema");
        assertEquals(visitedMap.get(schemaFalse), "false_schema");

        Schema<Object> anotherTrue = new Schema<>();
        anotherTrue.setBooleanSchemaValue(true);
        assertEquals(visitedMap.get(anotherTrue), "true_schema");

        Schema<Object> anotherFalse = new Schema<>();
        anotherFalse.setBooleanSchemaValue(false);
        assertEquals(visitedMap.get(anotherFalse), "false_schema");
    }

    @Test
    public void testSchemaResolutionWithBooleanSchemaValue() {
        Map<Schema<Object>, Schema<Object>> visitedMap = new HashMap<>();

        // Create a schema with booleanSchemaValue = false
        Schema<Object> originalSchema = new Schema<>();
        originalSchema.setBooleanSchemaValue(false);
        originalSchema.setTitle("Original Schema");
        originalSchema.setType("boolean");

        // Create a resolved version of the same schema
        Schema<Object> resolvedSchema = new Schema<>();
        resolvedSchema.setBooleanSchemaValue(false);
        resolvedSchema.setTitle("Resolved Schema");
        resolvedSchema.setType("boolean");

        // Put the original schema in the visited map
        visitedMap.put(originalSchema, resolvedSchema);

        // Create another schema with identical structure but booleanSchemaValue = true
        Schema<Object> differentSchema = new Schema<>();
        differentSchema.setBooleanSchemaValue(true);
        differentSchema.setTitle("Original Schema");
        differentSchema.setType("boolean");

        // The lookup should NOT return the resolved schema for the different schema
        assertNull(visitedMap.get(differentSchema));

        // But it should return the resolved schema for an equivalent schema with false value
        Schema<Object> equivalentSchema = new Schema<>();
        equivalentSchema.setBooleanSchemaValue(false);
        equivalentSchema.setTitle("Original Schema");
        equivalentSchema.setType("boolean");
        assertEquals(resolvedSchema, visitedMap.get(equivalentSchema));

        // Verify that the schemas with different booleanSchemaValue are indeed different
        assertNotEquals(originalSchema, differentSchema);
        assertNotEquals(originalSchema.hashCode(), differentSchema.hashCode());
    }

    @Test
    public void testBooleanSchemaValueInSetOperations() {
        java.util.Set<Schema<Object>> schemaSet = new java.util.HashSet<>();

        Schema<Object> schemaTrue = new Schema<>();
        schemaTrue.setBooleanSchemaValue(true);

        Schema<Object> schemaFalse = new Schema<>();
        schemaFalse.setBooleanSchemaValue(false);

        schemaSet.add(schemaTrue);
        schemaSet.add(schemaFalse);

        // Should have two distinct schemas in the set
        assertEquals(schemaSet.size(), 2);
        assertTrue(schemaSet.contains(schemaTrue));
        assertTrue(schemaSet.contains(schemaFalse));

        // Adding equivalent schemas should not increase the set size
        Schema<Object> anotherTrue = new Schema<>();
        anotherTrue.setBooleanSchemaValue(true);
        schemaSet.add(anotherTrue);
        assertEquals(schemaSet.size(), 2);
    }

    private Schema<Object> createComplexSchema() {
        Schema<Object> schema = new Schema<>();
        schema.setTitle("Complex Schema");
        schema.setType("object");
        schema.setDescription("A complex schema for testing");
        schema.setMaximum(new BigDecimal("100"));
        schema.setMinimum(new BigDecimal("0"));
        schema.setRequired(Arrays.asList("id", "name"));
        schema.setBooleanSchemaValue(true);
        return schema;
    }
}
