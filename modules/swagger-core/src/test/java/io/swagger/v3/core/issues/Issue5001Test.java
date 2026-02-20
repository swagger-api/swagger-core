package io.swagger.v3.core.issues;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import java.util.Set;

import static org.testng.Assert.*;

/**
 * Reproduces GitHub Issue #5001
 * Native support for @Nullable annotations to generate proper nullable types
 *
 * Tests that @Nullable annotation is recognized and generates appropriate nullable output:
 * - OAS 3.0: nullable keyword
 * - OAS 3.1: type array with "null"
 *
 * Note: This test uses javax.annotation.Nullable which is automatically transformed to
 * jakarta.annotation.Nullable in the swagger-core-jakarta module via the Eclipse Transformer.
 *
 * @see <a href="https://github.com/swagger-api/swagger-core/issues/5001">...</a>
 */
public class Issue5001Test {

    /**
     * Tests @Nullable annotation with OAS 3.1 (type array output)
     */
    @Test
    public void testNullableWithOAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        // Field with @Nullable should generate type array ["string", "null"]
        io.swagger.v3.oas.models.media.Schema nullableField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableString");
        assertNotNull(nullableField, "nullableString property should exist");
        assertNotNull(nullableField.getTypes(), "@Nullable should generate types array in OAS 3.1");
        assertTrue(nullableField.getTypes().contains("string"), "types should include 'string'");
        assertTrue(nullableField.getTypes().contains("null"), "types should include 'null'");
        assertEquals(((Set<?>) nullableField.getTypes()).size(), 2, "Should have exactly 2 types");

        // Non-nullable field should only have string type
        io.swagger.v3.oas.models.media.Schema requiredField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("requiredString");
        assertNotNull(requiredField);
        assertNotNull(requiredField.getTypes());
        assertTrue(requiredField.getTypes().contains("string"));
        assertFalse(requiredField.getTypes().contains("null"), "Non-nullable field should not have 'null' type");
    }

    /**
     * Tests @Nullable annotation with OAS 3.0 (nullable keyword output)
     */
    @Test
    public void testNullableWithOAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        // Field with @Nullable should set nullable=true in OAS 3.0
        io.swagger.v3.oas.models.media.Schema nullableField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableString");
        assertNotNull(nullableField, "nullableString property should exist");
        assertEquals(nullableField.getNullable(), Boolean.TRUE, "@Nullable should set nullable=true in OAS 3.0");
        assertEquals(nullableField.getType(), "string", "type should be 'string'");

        // Non-nullable field should not have nullable property
        io.swagger.v3.oas.models.media.Schema requiredField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("requiredString");
        assertNotNull(requiredField);
        assertNotEquals(requiredField.getNullable(), Boolean.TRUE, "Non-nullable field should not be nullable");
    }

    /**
     * Tests explicit @Schema annotations with OAS 3.1
     */
    @Test
    public void testExplicitSchemaAnnotationsWithOAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(ExplicitSchemaModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        // @Schema(nullable=true) should set nullable property and generate types array
        io.swagger.v3.oas.models.media.Schema explicitNullable =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("explicitNullableString");
        assertNotNull(explicitNullable);
        assertEquals(explicitNullable.getNullable(), Boolean.TRUE, "@Schema(nullable=true) should set nullable");
        assertTrue(explicitNullable.getTypes().contains("string"));
        assertTrue(explicitNullable.getTypes().contains("null"));

        // @Schema(types={"string", "null"}) should work
        io.swagger.v3.oas.models.media.Schema explicitTypes =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("explicitTypesString");
        assertNotNull(explicitTypes);
        assertNotNull(explicitTypes.getTypes());
        assertTrue(explicitTypes.getTypes().contains("string"));
        assertTrue(explicitTypes.getTypes().contains("null"));
    }

    /**
     * Model using @Nullable annotation
     * Note: Uses javax.annotation.Nullable which gets transformed to jakarta.annotation.Nullable
     * in the swagger-core-jakarta module
     */
    public static class NullableModel {
        @Nullable
        private String nullableString;

        private String requiredString;

        public String getNullableString() {
            return nullableString;
        }

        public void setNullableString(String nullableString) {
            this.nullableString = nullableString;
        }

        public String getRequiredString() {
            return requiredString;
        }

        public void setRequiredString(String requiredString) {
            this.requiredString = requiredString;
        }
    }

    /**
     * Model using explicit @Schema annotations
     */
    public static class ExplicitSchemaModel {
        @Schema(nullable = true)
        private String explicitNullableString;

        @Schema(types = {"string", "null"})
        private String explicitTypesString;

        public String getExplicitNullableString() {
            return explicitNullableString;
        }

        public void setExplicitNullableString(String explicitNullableString) {
            this.explicitNullableString = explicitNullableString;
        }

        public String getExplicitTypesString() {
            return explicitTypesString;
        }

        public void setExplicitTypesString(String explicitTypesString) {
            this.explicitTypesString = explicitTypesString;
        }
    }
}
