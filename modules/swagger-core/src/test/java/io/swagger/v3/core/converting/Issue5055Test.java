package io.swagger.v3.core.converting;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

/**
 * test documenting the behavior of sibling @Schema and @ArraySchema annotations.
 */
public class Issue5055Test {


    @Test
    public void testArrayMetadataDoesNotLeakToItemsRef() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(true).readAllAsResolvedSchema(
            TestModels.ArrayWithFullMetadata.class
        );
        
        assertNotNull(schema, "Schema should resolve");
        String json = Json31.pretty(schema);
        assertNotNull(json);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        
        JsonNode itemsProp = root.at("/schema/properties/pets");
        assertFalse(itemsProp.isMissingNode(), "pets property should exist");
        
        assertEquals(itemsProp.get("type").asText(), "array", "Should be array type");
        assertEquals(itemsProp.get("description").asText(), "Collection of pets", "Array should have description");
        assertEquals(itemsProp.get("minItems").asInt(), 1, "Array should have minItems");
        assertEquals(itemsProp.get("maxItems").asInt(), 100, "Array should have maxItems");
        assertTrue(itemsProp.get("uniqueItems").asBoolean(), "Array should have uniqueItems");
        
        JsonNode items = itemsProp.get("items");
        assertNotNull(items, "Items should exist");
        assertTrue(items.has("$ref"), "Items should be a reference");
        
        assertFalse(items.has("minItems"), "BUG: minItems leaked to items $ref");
        assertFalse(items.has("maxItems"), "BUG: maxItems leaked to items $ref");
        assertFalse(items.has("uniqueItems"), "BUG: uniqueItems leaked to items $ref");
        assertFalse(items.has("description") && items.get("description") != null, 
            "BUG: array description leaked to items $ref");
        
        if (items.has("type")) {
            assertTrue(items.get("type").isNull() || items.get("type").asText().isEmpty(),
                "BUG: type value leaked to items $ref");
        }
    }

    @Test
    public void testComponentSchemaIsCleanWithoutLeakedProperties() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(true).readAllAsResolvedSchema(
            TestModels.ArrayWithFullMetadata.class
        );
        
        assertNotNull(schema);
        assertNotNull(schema.referencedSchemas);
        
        io.swagger.v3.oas.models.media.Schema petSchema = schema.referencedSchemas.get("Pet");
        assertNotNull(petSchema, "Pet component schema should exist");
        
        assertNull(petSchema.getMinItems(), "Component schema should not have minItems");
        assertNull(petSchema.getMaxItems(), "Component schema should not have maxItems");
        assertNull(petSchema.getUniqueItems(), "Component schema should not have uniqueItems");
        
        if (petSchema.getDescription() != null) {
            assertNotEquals(petSchema.getDescription(), "Collection of pets",
                "Component should not have array's description");
        }
    }

    @Test
    public void testArraySchemaAttributesSeparation() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(true).readAllAsResolvedSchema(
            TestModels.BothArraySchemaAttributes.class
        );
        
        assertNotNull(schema);
        String json = Json31.pretty(schema);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        
        JsonNode dataField = root.at("/schema/properties/data");
        if (!dataField.isMissingNode()) {
            assertTrue(dataField.has("description"));
            assertEquals(dataField.get("type").asText(), "array");
            
            JsonNode items = dataField.get("items");
            assertNotNull(items);
            assertTrue(items.has("description") || items.has("$ref"));
        }
    }

    @Test
    public void testTypeInferenceWithNoImplementation() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(true).readAllAsResolvedSchema(
            TestModels.NoImplementationSpecified.class
        );
        
        assertNotNull(schema);
        String json = Json31.pretty(schema);
        
        System.out.println("\n=== testTypeInferenceWithNoImplementation - Generated Spec ===");
        System.out.println(json);
        System.out.println("=== End Spec ===\n");
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        
        JsonNode dataField = root.at("/schema/properties/data");
        assertEquals(dataField.get("type").asText(), "array");
            
        JsonNode items = dataField.get("items");
        assertNotNull(items);
        if (items.has("type")) {
            assertEquals(items.get("type").asText(), "string");
        }

    }

    @Test
    public void testSchemaEqualsSemanticsForPrecedence() {
        io.swagger.v3.oas.models.media.Schema unchanged = new io.swagger.v3.oas.models.media.Schema();
        unchanged.setType("array");
        
        io.swagger.v3.oas.models.media.Schema alsoUnchanged = new io.swagger.v3.oas.models.media.Schema();
        alsoUnchanged.setType("array");
        
        assertEquals(unchanged, alsoUnchanged, "Equal content should be detected");
        
        io.swagger.v3.oas.models.media.Schema changed = new io.swagger.v3.oas.models.media.Schema();
        changed.setType("array");
        changed.setDescription("added");
        
        assertNotEquals(unchanged, changed, "Changes should be detected by equals");
    }

    @Test
    public void testSchemaEqualsIncludesExtensions() {
        io.swagger.v3.oas.models.media.Schema s1 = new io.swagger.v3.oas.models.media.Schema();
        s1.addExtension("x-prop", "value");
        
        io.swagger.v3.oas.models.media.Schema s2 = new io.swagger.v3.oas.models.media.Schema();
        s2.addExtension("x-prop", "value");
        
        assertEquals(s1, s2, "Same extensions should be equal");
        
        io.swagger.v3.oas.models.media.Schema s3 = new io.swagger.v3.oas.models.media.Schema();
        s3.addExtension("x-prop", "different");
        
        assertNotEquals(s1, s3, "Different extensions should not be equal");
    }

    @Test
    public void testNoImplementationCase() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(true).readAllAsResolvedSchema(
            TestModels.NoImplementationSpecified.class
        );
        
        assertNotNull(schema);
        String json = Json31.pretty(schema);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        
        JsonNode dataField = root.at("/schema/properties/data");
        assertFalse(dataField.isMissingNode(), "data property should exist");
        assertEquals(dataField.get("type").asText(), "array");
        assertEquals(dataField.get("description").asText(), "Inferred from type");
        
        JsonNode items = dataField.get("items");
        assertNotNull(items);
        assertEquals(items.get("type").asText(), "string", "Items should infer string type");
    }

    @Test
    public void testNoSchemaAtAllCase() throws Exception {
        ResolvedSchema schema = ModelConverters.getInstance(true).readAllAsResolvedSchema(
            TestModels.NoSchemaAnnotations.class
        );
        
        assertNotNull(schema);
        String json = Json31.pretty(schema);
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);
        
        JsonNode dataField = root.at("/schema/properties/data");
        assertFalse(dataField.isMissingNode(), "data property should exist");
        assertEquals(dataField.get("type").asText(), "array");
        
        JsonNode items = dataField.get("items");
        assertNotNull(items);
        assertEquals(items.get("type").asText(), "string", "Items should infer string type from List<String>");
    }


    public static class TestModels {
        
        @io.swagger.v3.oas.annotations.media.Schema(description = "Model with full array metadata")
        public static class ArrayWithFullMetadata {
            private List<Pet> pets;
            
            @ArraySchema(
                schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Pet.class),
                arraySchema = @io.swagger.v3.oas.annotations.media.Schema(
                    type = "array",
                    description = "Collection of pets"
                ),
                minItems = 1,
                maxItems = 100,
                uniqueItems = true
            )
            public List<Pet> getPets() {
                return pets;
            }
        }
        
        @io.swagger.v3.oas.annotations.media.Schema(description = "Pet model")
        public static class Pet {
            private String name;
            private String species;
            
            public String getName() {
                return name;
            }
            
            public String getSpecies() {
                return species;
            }
        }
        
        @io.swagger.v3.oas.annotations.media.Schema(description = "Test precedence")
        public static class SchemaTakesPrecedence {
            private List<String> data;
            
            @io.swagger.v3.oas.annotations.media.Schema(type = "array", description = "Schema description")
            @ArraySchema(minItems = 10)
            public List<String> getData() {
                return data;
            }
        }
        
        @io.swagger.v3.oas.annotations.media.Schema(description = "Test both attributes")
        public static class BothArraySchemaAttributes {
            private List<String> data;
            
            @ArraySchema(
                schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string", description = "Item description"),
                arraySchema = @io.swagger.v3.oas.annotations.media.Schema(type = "array", description = "Array description")
            )
            public List<String> getData() {
                return data;
            }
        }
        
        @io.swagger.v3.oas.annotations.media.Schema(description = "Test no implementation")
        public static class NoImplementationSpecified {
            private List<String> data;
            
            @io.swagger.v3.oas.annotations.media.Schema(description = "Inferred from type")
            public List<String> getData() {
                return data;
            }
        }
        
        @io.swagger.v3.oas.annotations.media.Schema(description = "Test no schema annotations")
        public static class NoSchemaAnnotations {
            private List<String> data;
            
            public List<String> getData() {
                return data;
            }
        }
    }
}
