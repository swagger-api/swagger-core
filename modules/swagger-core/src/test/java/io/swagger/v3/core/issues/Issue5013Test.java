package io.swagger.v3.core.issues;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import java.util.stream.Stream;

import static org.testng.Assert.*;

/**
 * Reproduces GitHub Issue #5013
 * @ArraysSchema applied on a stream property results in object type in OAS 3.1
 * <p>
 * Bug: When upgrading from OpenAPI 3.0 to 3.1, Stream properties annotated with
 * @ArraySchema are incorrectly generated as type 'object' instead of 'array'.
 * The internal Java Stream.parallel property is exposed in the schema.
 * <p>
 * {@see https://github.com/swagger-api/swagger-core/issues/5012}
 */
public class Issue5013Test {

    /**
     * Test 1: Stream property generates as object instead of array in OAS 3.1
     * <p>
     * Tests that a Stream<T> property with @ArraySchema should generate as type 'array'
     * in OAS 3.1, not as type 'object' with exposed Stream.parallel property.
     */
    @Test
    public void testStreamPropertyGeneratesAsObjectInOAS31() {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(ModelWithStream.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema greetingsProperty =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("greetings");
        assertNotNull(greetingsProperty, "greetings property should exist");

        // BUG: Stream is generated as 'object' instead of 'array'
        assertEquals(greetingsProperty.getType(), "array",
                "Stream with @ArraySchema should generate as type 'array'"); // FAILS - currently 'object'

        // BUG: Stream.parallel property is exposed (should not be)
        if (greetingsProperty.getProperties() != null) {
            assertNull(greetingsProperty.getProperties().get("parallel"),
                    "Stream.parallel property should NOT be exposed in schema"); // FAILS - currently exposed
        }

        // Verify items reference is correct
        assertNotNull(greetingsProperty.getItems(), "items should be present");

        io.swagger.v3.oas.models.media.Schema items = greetingsProperty.getItems();
        assertNotNull(items.get$ref(), "items should have $ref to Greeting");
        assertTrue(items.get$ref().contains("Greeting"),
                "items should reference Greeting schema");
    }

    /**
     * Test 2: Stream property works correctly in OAS 3.0 (baseline)
     * <p>
     * Validates that the same model generates correctly with OpenAPI 3.0,
     * confirming this is a regression specific to OAS 3.1.
     */
    @Test
    public void testStreamPropertyWorksInOAS30() {
        // Use OAS 3.0 mapper instead of 3.1
        final ModelResolver modelResolver = new ModelResolver(io.swagger.v3.core.util.Json.mapper());
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(ModelWithStream.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema greetingsProperty =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("greetings");
        assertNotNull(greetingsProperty);

        // In OAS 3.0, this should work correctly
        assertEquals(greetingsProperty.getType(), "array",
                "In OAS 3.0, Stream should generate as type 'array'");

        // Verify no parallel property is exposed
        if (greetingsProperty.getProperties() != null) {
            assertNull(greetingsProperty.getProperties().get("parallel"),
                    "Stream.parallel property should NOT be exposed");
        }
    }

    // Minimal test models - Java 11 compatible syntax

    /**
     * Model with Stream property - demonstrates the bug
     */
    public static class ModelWithStream {

        @ArraySchema(schema = @Schema(implementation = Greeting.class))
        private Stream<Greeting> greetings;

        public Stream<Greeting> getGreetings() {
            return greetings;
        }

        public void setGreetings(Stream<Greeting> greetings) {
            this.greetings = greetings;
        }
    }

    /**
     * Simple Greeting model for testing
     */
    public static class Greeting {

        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
