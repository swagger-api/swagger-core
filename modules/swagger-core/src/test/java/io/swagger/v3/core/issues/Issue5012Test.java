package io.swagger.v3.core.issues;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Reproduces GitHub Issue #5012
 * The OAS 3.1 is missing information from the @ArraysSchema.arraySchema
 * <p>
 * Bug: When switching from OpenAPI 3.0 to 3.1, some @Schema properties defined in
 * ArraySchema.arraySchema are not generated into the OpenAPI JSON spec:
 * - readOnly
 * - accessMode
 * - examples
 * - deprecated
 * <p>
 * {@see https://github.com/swagger-api/swagger-core/issues/5012}
 */
public class Issue5012Test {

    /**
     * Test 1: ArraySchema.arraySchema properties missing in OAS 3.1
     * <p>
     * Tests that deprecated, readOnly, and examples from arraySchema are included
     * in the generated OAS 3.1 schema for array properties.
     */
    @Test
    public void testArraySchemaPropertiesMissingInOAS31() {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper()).openapi31(true);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(MyModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema namesProperty = (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("names");
        assertNotNull(namesProperty, "names property should exist");

        // Verify array type
        Set<String> expectedArrayType = new java.util.HashSet<>(Collections.singleton("array"));
        assertEquals(namesProperty.getTypes(), expectedArrayType, "names should be of type array");

        // Verify arraySchema properties
        assertEquals(namesProperty.getDescription(), "collection description",
                "arraySchema description should be present");

        assertEquals(namesProperty.getDeprecated(), Boolean.TRUE,
                "arraySchema deprecated should be true");

        assertEquals(namesProperty.getReadOnly(), Boolean.TRUE,
                "arraySchema readOnly (from accessMode) should be true");

        assertEquals(namesProperty.getExample(), "John",
                "arraySchema example should be 'John'");

        io.swagger.v3.oas.models.media.Schema itemsSchema = namesProperty.getItems();
        assertNotNull(itemsSchema, "items schema should exist");
        Set<String> expectedItemTypes = new java.util.HashSet<>(Collections.singleton("string"));
        assertEquals(itemsSchema.getTypes(), expectedItemTypes, "items should be of type string");
        assertEquals(itemsSchema.getDescription(), "item description",
                "item description should be present");
        assertEquals(itemsSchema.getExample(), "Jason",
                "item example should be 'Jason'");
    }

    /**
     * Test 2: Workaround - Switching to OAS 3.0 works correctly
     * <p>
     * Validates that the same model generates correctly with OpenAPI 3.0,
     * confirming this is a regression in OAS 3.1 handling.
     */
    @Test
    public void testArraySchemaWorksInOAS30() {
        // Use OAS 3.0 mapper instead of 3.1
        final ModelResolver modelResolver = new ModelResolver(io.swagger.v3.core.util.Json.mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(MyModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema namesProperty = (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("names");
        assertNotNull(namesProperty);

        // In OAS 3.0, these properties should be present
        assertEquals(namesProperty.getDescription(), "collection description");

        assertEquals(namesProperty.getDeprecated(), Boolean.TRUE,
                "arraySchema deprecated should be true");

        assertEquals(namesProperty.getReadOnly(), Boolean.TRUE,
                "arraySchema readOnly (from accessMode) should be true");

        assertNotNull(namesProperty.getExample(),
                "arraySchema examples should be present");
        assertEquals(namesProperty.getExample(), "John",
                "arraySchema example should be 'John'");
    }

    // Minimal test model - only what's needed to reproduce the bug
    static class MyModel {
        @ArraySchema(
                arraySchema = @Schema(
                        deprecated = true,
                        accessMode = Schema.AccessMode.READ_ONLY,
                        description = "collection description",
                        example = "John"),  // Using example for OAS 3.0 compatibility
                schema = @Schema(
                        description = "item description",
                        example = "Jason"))
        private List<String> names;

        public List<String> getNames() {
            return names;
        }

        public void setNames(List<String> names) {
            this.names = names;
        }
    }
}