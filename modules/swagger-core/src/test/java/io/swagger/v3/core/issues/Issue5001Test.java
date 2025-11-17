package io.swagger.v3.core.issues;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Reproduces GitHub Issue #5001
 * Native support for Jakarta @Nullable annotation to generate proper OAS 3.1 nullable types
 *
 * Bug: Fields annotated with @Nullable are not automatically recognized as nullable
 * in the generated OpenAPI schema. Users must explicitly add @Schema(nullable = true)
 * or @Schema(types = {"string", "null"}), creating annotation redundancy.
 *
 * @see <a href="https://github.com/swagger-api/swagger-core/issues/5001">...</a>
 */
public class Issue5001Test {

    /**
     * Test 1: @Nullable annotation not recognized in OAS 3.1
     * 
     * Tests that a field annotated only with @Nullable should generate a nullable
     * type in OpenAPI 3.1 (type: ["string", "null"]) without requiring explicit
     * @Schema annotations.
     */
    @Test
    public void testNullableAnnotationNotRecognized() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(ExampleModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        // Test field with only @Nullable annotation
        io.swagger.v3.oas.models.media.Schema nullableField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("stringThatCouldBeNull");
        assertNotNull(nullableField, "stringThatCouldBeNull property should exist");

        // BUG: @Nullable is not recognized, so types array is not generated
        // Expected: types should be ["string", "null"] in OAS 3.1
        assertNotNull(nullableField.getTypes(), "@Nullable should generate types array");

        assertTrue(nullableField.getTypes().contains("string"), "types should include 'string'");
        assertTrue(nullableField.getTypes().contains("null"), "types should include 'null'");

        // Test field with explicit @Schema(nullable = true)
        io.swagger.v3.oas.models.media.Schema explicitNullable =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("explicitNullableString");
        assertNotNull(explicitNullable, "explicitNullableString property should exist");

        // This should work (explicit annotation)
        assertEquals(explicitNullable.getNullable(), Boolean.TRUE,
                "Explicit @Schema(nullable=true) should work");
        assertTrue(explicitNullable.getTypes().contains("string"), "types should include 'string'");
        assertTrue(explicitNullable.getTypes().contains("null"), "types should include 'null'");

        // Test field with both @Nullable and @Schema(types)
        io.swagger.v3.oas.models.media.Schema bothAnnotations = 
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("bothAnnotationsString");
        assertNotNull(bothAnnotations, "bothAnnotationsString property should exist");
        
        // This should work (explicit types)
        assertNotNull(bothAnnotations.getTypes(),
                "Explicit @Schema(types) should generate types array");
        assertTrue(bothAnnotations.getTypes().contains("string"), "types should include 'string'");
        assertTrue(bothAnnotations.getTypes().contains("null"), "types should include 'null'");
    }

    /**
     * Test 2: Explicit @Schema annotations work correctly (baseline)
     * 
     * Validates that explicit @Schema annotations work correctly to establish
     * baseline behavior for comparison.
     */
    @Test
    public void testExplicitSchemaAnnotationsWork() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(ExampleModel.class));

        assertNotNull(model);
        
        // Verify explicit @Schema(nullable=true) works
        io.swagger.v3.oas.models.media.Schema explicitField = 
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("explicitNullableString");
        
        assertEquals(explicitField.getNullable(), Boolean.TRUE,
                "Explicit @Schema(nullable=true) should set nullable property");
        
        // Verify explicit @Schema(types=...) works
        io.swagger.v3.oas.models.media.Schema typesField = 
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("bothAnnotationsString");
        
        assertNotNull(typesField.getTypes(),
                "Explicit @Schema(types) should generate types array");
        
        Set<String> types = (Set<String>) typesField.getTypes();
        assertTrue(types.contains("string"), "Should include 'string' type");
        assertTrue(types.contains("null"), "Should include 'null' type");
        assertEquals(types.size(), 2, "Should have 2 types");

    }

    // Minimal test model - Java 11 compatible syntax
    
    /**
     * Model demonstrating @Nullable annotation handling
     */
    public static class ExampleModel {
        
        // Field with only Jakarta @Nullable - BUG: not recognized
        @Nullable
        private String stringThatCouldBeNull;

        // Field with explicit @Schema(nullable = true) - works correctly
        @Schema(nullable = true)
        private String explicitNullableString;

        // Field with both @Nullable and @Schema(types) - works correctly
        @Nullable
        @Schema(types = {"string", "null"})
        private String bothAnnotationsString;

        // Regular non-nullable field for comparison
        private String requiredString;

        // Getters and setters (Java 11 compatible)
        
        public String getStringThatCouldBeNull() {
            return stringThatCouldBeNull;
        }

        public void setStringThatCouldBeNull(String stringThatCouldBeNull) {
            this.stringThatCouldBeNull = stringThatCouldBeNull;
        }

        public String getExplicitNullableString() {
            return explicitNullableString;
        }

        public void setExplicitNullableString(String explicitNullableString) {
            this.explicitNullableString = explicitNullableString;
        }

        public String getBothAnnotationsString() {
            return bothAnnotationsString;
        }

        public void setBothAnnotationsString(String bothAnnotationsString) {
            this.bothAnnotationsString = bothAnnotationsString;
        }

        public String getRequiredString() {
            return requiredString;
        }

        public void setRequiredString(String requiredString) {
            this.requiredString = requiredString;
        }
    }
}
