package io.swagger.v3.core.issues;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverterContextImpl;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Configuration;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.annotations.media.Schema;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.*;

/**
 * Reproduces GitHub Issue #4339
 * Schema annotation processing isn't working with nullable and "null" example or default value
 *
 * Expected behavior:
 * All data types should respect nullable=true and produce null values in example/default fields
 * when example="null" or defaultValue="null" is specified.
 *
 * Tests both OAS 3.0 and OAS 3.1 specifications.
 *
 * @see <a href="https://github.com/swagger-api/swagger-core/issues/4339">...</a>
 */
public class Issue4339Test {

    //OAS 3.0 Tests
    @Test
    public void testNullableStringWithNullExampleAndDefault_OAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableStringModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema nullableStringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableStringField");
        assertNotNull(nullableStringField, "nullableStringField property should exist");
        assertEquals(nullableStringField.getNullable(), Boolean.TRUE, "Field should be nullable");
        
        assertNull(nullableStringField.getExample(),
            "Example should be null for nullable field with example=\"null\", but got: " + nullableStringField.getExample());
        assertNull(nullableStringField.getDefault(), 
            "Default should be null for nullable field with defaultValue=\"null\", but got: " + nullableStringField.getDefault());
    }

    @Test
    public void testNullableIntegerWithNullExampleAndDefault_OAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableIntegerModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema nullableIntegerField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableIntegerField");
        assertNotNull(nullableIntegerField, "nullableIntegerField property should exist");
        assertEquals(nullableIntegerField.getNullable(), Boolean.TRUE, "Field should be nullable");
        
        assertNull(nullableIntegerField.getExample(),
            "Example should be null for nullable integer field with example=\"null\"");
        assertNull(nullableIntegerField.getDefault(), 
            "Default should be null for nullable integer field with defaultValue=\"null\"");
    }


    @Test
    public void testNullableBigDecimalWithNullExampleAndDefault_OAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableBigDecimalModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema nullableBigDecimalField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableBigDecimalField");
        assertNotNull(nullableBigDecimalField, "nullableBigDecimalField property should exist");
        assertEquals(nullableBigDecimalField.getNullable(), Boolean.TRUE, "Field should be nullable");
        
        assertNull(nullableBigDecimalField.getExample(),
            "Example should be null for nullable number field with example=\"null\"");
        assertNull(nullableBigDecimalField.getDefault(), 
            "Default should be null for nullable number field with defaultValue=\"null\"");
    }

    @Test
    public void testNullableBooleanWithNullExampleAndDefault_OAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableBooleanModel.class));


        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema nullableBooleanField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableBooleanField");
        assertNotNull(nullableBooleanField, "nullableBooleanField property should exist");
        assertEquals(nullableBooleanField.getNullable(), Boolean.TRUE, "Field should be nullable");

        assertNull(nullableBooleanField.getExample(), 
            "Example should be null for nullable boolean field with example=\"null\", but got: " + nullableBooleanField.getExample());
        assertNull(nullableBooleanField.getDefault(), 
            "Default should be null for nullable boolean field with defaultValue=\"null\", but got: " + nullableBooleanField.getDefault());
    }

    @Test
    public void testNullableObjectWithNullExampleAndDefault_OAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableObjectModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema nullableObjectField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableObjectField");
        assertNotNull(nullableObjectField, "nullableObjectField property should exist");
        assertEquals(nullableObjectField.getNullable(), Boolean.TRUE, "Field should be nullable");
        
        assertNull(nullableObjectField.getExample(), "Object example should be null");
        assertNull(nullableObjectField.getDefault(), "Object default should be null");
    }

    // OAS 3.1 Tests


    @Test
    public void testNullableStringWithNullExampleAndDefault_OAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableStringModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema nullableStringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableStringField");
        assertNotNull(nullableStringField, "nullableStringField property should exist");
        assertTrue(isNullableInOAS31(nullableStringField), "Field should be nullable (type array should contain 'null')");

        assertNull(nullableStringField.getExample(), 
            "Example should be null for nullable field with example=\"null\", but got: " + nullableStringField.getExample());
        assertNull(nullableStringField.getDefault(), 
            "Default should be null for nullable field with defaultValue=\"null\", but got: " + nullableStringField.getDefault());
    }

    @Test
    public void testNullableIntegerWithNullExampleAndDefault_OAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableIntegerModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema nullableIntegerField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableIntegerField");
        assertNotNull(nullableIntegerField, "nullableIntegerField property should exist");
        assertTrue(isNullableInOAS31(nullableIntegerField), "Field should be nullable (type array should contain 'null')");
        
        assertNull(nullableIntegerField.getExample(),
            "Example should be null for nullable integer field with example=\"null\"");
        assertNull(nullableIntegerField.getDefault(), 
            "Default should be null for nullable integer field with defaultValue=\"null\"");
    }

    @Test
    public void testNullableBigDecimalWithNullExampleAndDefault_OAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableBigDecimalModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema nullableBigDecimalField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableBigDecimalField");
        assertNotNull(nullableBigDecimalField, "nullableBigDecimalField property should exist");
        assertTrue(isNullableInOAS31(nullableBigDecimalField), "Field should be nullable (type array should contain 'null')");
        
        assertNull(nullableBigDecimalField.getExample(),
            "Example should be null for nullable number field with example=\"null\"");
        assertNull(nullableBigDecimalField.getDefault(), 
            "Default should be null for nullable number field with defaultValue=\"null\"");
    }


    @Test
    public void testNullableBooleanWithNullExampleAndDefault_OAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableBooleanModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema nullableBooleanField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableBooleanField");
        assertNotNull(nullableBooleanField, "nullableBooleanField property should exist");
        assertTrue(isNullableInOAS31(nullableBooleanField), "Field should be nullable (type array should contain 'null')");
        

        assertNull(nullableBooleanField.getExample(), 
            "Example should be null for nullable boolean field with example=\"null\", but got: " + nullableBooleanField.getExample());
        assertNull(nullableBooleanField.getDefault(), 
            "Default should be null for nullable boolean field with defaultValue=\"null\", but got: " + nullableBooleanField.getDefault());
    }


    @Test
    public void testNullableObjectWithNullExampleAndDefault_OAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableObjectModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema nullableObjectField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("nullableObjectField");
        assertNotNull(nullableObjectField, "nullableObjectField property should exist");
        assertTrue(isNullableInOAS31(nullableObjectField), "Field should be nullable (type array should contain 'null')");
        
        assertNull(nullableObjectField.getExample(), "Object example should be null");
        assertNull(nullableObjectField.getDefault(), "Object default should be null");
    }

    // Tests for omitted example/default values


    @Test
    public void testNullableWithoutExampleOrDefault_OAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableWithoutExampleDefaultModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema stringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("stringField");
        assertNotNull(stringField, "stringField property should exist");
        assertEquals(stringField.getNullable(), Boolean.TRUE, "Field should be nullable");
        assertNull(stringField.getExample(), "Example should be null (not set)");
        assertNull(stringField.getDefault(), "Default should be null (not set)");

        io.swagger.v3.oas.models.media.Schema integerField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("integerField");
        assertNotNull(integerField, "integerField property should exist");
        assertEquals(integerField.getNullable(), Boolean.TRUE, "Field should be nullable");
        assertNull(integerField.getExample(), "Example should be null (not set)");
        assertNull(integerField.getDefault(), "Default should be null (not set)");

        io.swagger.v3.oas.models.media.Schema booleanField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("booleanField");
        assertNotNull(booleanField, "booleanField property should exist");
        assertEquals(booleanField.getNullable(), Boolean.TRUE, "Field should be nullable");
        assertNull(booleanField.getExample(), "Example should be null (not set)");
        assertNull(booleanField.getDefault(), "Default should be null (not set)");
    }

    @Test
    public void testNullableWithoutExampleOrDefault_OAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableWithoutExampleDefaultModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema stringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("stringField");
        assertNotNull(stringField, "stringField property should exist");
        assertTrue(isNullableInOAS31(stringField), "Field should be nullable (type array should contain 'null')");
        assertNull(stringField.getExample(), "Example should be null (not set)");
        assertNull(stringField.getDefault(), "Default should be null (not set)");

        io.swagger.v3.oas.models.media.Schema integerField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("integerField");
        assertNotNull(integerField, "integerField property should exist");
        assertTrue(isNullableInOAS31(integerField), "Field should be nullable (type array should contain 'null')");
        assertNull(integerField.getExample(), "Example should be null (not set)");
        assertNull(integerField.getDefault(), "Default should be null (not set)");

        io.swagger.v3.oas.models.media.Schema booleanField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("booleanField");
        assertNotNull(booleanField, "booleanField property should exist");
        assertTrue(isNullableInOAS31(booleanField), "Field should be nullable (type array should contain 'null')");
        assertNull(booleanField.getExample(), "Example should be null (not set)");
        assertNull(booleanField.getDefault(), "Default should be null (not set)");
    }

    @Test
    public void testNullableWithDefaultOnly_OAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableWithDefaultOnlyModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema stringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("stringField");
        assertNotNull(stringField, "stringField property should exist");
        assertNull(stringField.getExample(), "Example should be null (not set)");
        assertEquals(stringField.getDefault(), "defaultValue", "Default should be 'defaultValue'");
    }

    @Test
    public void testNullableWithExampleOnly_OAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableWithExampleOnlyModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema stringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("stringField");
        assertNotNull(stringField, "stringField property should exist");
        assertEquals(stringField.getExample(), "exampleValue", "Example should be 'exampleValue'");
        assertNull(stringField.getDefault(), "Default should be null (not set)");
    }

    // ========== Tests for nullable=false with example/default="null" ==========

    /**
     * Test non-nullable field with example="null" and defaultValue="null" - OAS 3.0
     * When nullable=false, "null" string should NOT be parsed as null value
     * It should be treated as the literal string "null"
     */
    @Test
    public void testNonNullableStringWithNullExampleAndDefault_OAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NonNullableStringModel.class));


        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema stringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("stringField");
        assertNotNull(stringField, "stringField property should exist");
        assertNull(stringField.getNullable(), "Field should not have nullable set (or be false)");

        // When nullable=false, "null" string should be treated as literal string "null", not null value
        assertEquals(stringField.getExample(), "null",
                "Example should be the string \"null\", not null value when nullable=false");
        assertEquals(stringField.getDefault(), "null",
                "Default should be the string \"null\", not null value when nullable=false");
    }

    /**
     * Test non-nullable field with example="null" and defaultValue="null" - OAS 3.1
     * When nullable=false, "null" string should NOT be parsed as null value
     */
    @Test
    public void testNonNullableStringWithNullExampleAndDefault_OAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NonNullableStringModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema stringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("stringField");
        assertNotNull(stringField, "stringField property should exist");
        assertNull(stringField.getNullable(), "Field should not have nullable set in OAS 3.1");

        // When nullable=false, "null" string should be treated as literal string "null", not null value
        assertEquals(stringField.getExample(), "null",
                "Example should be the string \"null\", not null value when nullable=false");
        assertEquals(stringField.getDefault(), "null",
                "Default should be the string \"null\", not null value when nullable=false");
    }

    /**
     * Test non-nullable Boolean field - OAS 3.0
     */
    @Test
    public void testNonNullableBooleanWithNullExampleAndDefault_OAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NonNullableBooleanModel.class));


        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema booleanField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("booleanField");
        assertNotNull(booleanField, "booleanField property should exist");
        assertNull(booleanField.getNullable(), "Field should not have nullable set");

        assertEquals(booleanField.getExample(), false,
                "Should convert to false");
        assertEquals(booleanField.getDefault(), false,
                "Should convert to false");
    }

    /**
     * Test non-nullable Integer field - OAS 3.1
     * Should treat "null" as string, not parse to null value
     */
    @Test
    public void testNonNullableIntegerWithNullExampleAndDefault_OAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NonNullableIntegerModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema integerField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("integerField");
        assertNotNull(integerField, "integerField property should exist");

        assertEquals(integerField.getExample(), "null",
                "Example should be the string \"null\", not null value");
        assertEquals(integerField.getDefault(), "null",
                "Default should be the string \"null\", not null value");
    }

    // ========== Test for Issue #4229 (same root cause as #4339) ==========

    /**
     * Test case from Issue #4229: Nullable Integer ignores example value "null" and defaults to 0
     * https://github.com/swagger-api/swagger-core/issues/4229
     * This should now be fixed by the same changes that fix #4339
     */
    @Test
    public void testIssue4229_NullableIntegerWithNullExample() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(Issue4229TestData.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema idSchema =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("id");
        assertNotNull(idSchema, "id property should exist");
        assertEquals(idSchema.getNullable(), Boolean.TRUE, "Field should be nullable");

        // Issue #4229: exampleSetFlag should be true and example should be null, not 0
        assertTrue(idSchema.getExampleSetFlag(),
                "exampleSetFlag should be true when example=\"null\" is set");
        assertNull(idSchema.getExample(),
                "Example should be null, not 0 (fixes issue #4229)");
    }

    // ========== Tests for OAS 3.1 "examples" array field ==========

    /**
     * Test OAS 3.1 examples array with null value
     * In OAS 3.1, there's an "examples" array field in addition to the single "example" field
     */
    @Test
    public void testNullableStringWithNullInExamplesArray_OAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableStringWithExamplesModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema stringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("stringField");
        assertNotNull(stringField, "stringField property should exist");
        assertTrue(isNullableInOAS31(stringField), "Field should be nullable (type array should contain 'null')");

        assertNotNull(stringField.getExamples(), "examples array should exist");
        assertEquals(stringField.getExamples().size(), 3, "examples array should have 3 elements");

        assertTrue(stringField.getExamples().contains("validValue"), "examples should contain 'validValue'");
        assertTrue(stringField.getExamples().contains(null), "examples should contain null value");
        assertTrue(stringField.getExamples().contains("anotherValue"), "examples should contain 'anotherValue'");
    }

    /**
     * Test OAS 3.1 examples array with multiple values including null
     */
    @Test
    public void testNullableIntegerWithMultipleExamplesIncludingNull_OAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableIntegerWithMultipleExamplesModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema integerField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("integerField");
        assertNotNull(integerField, "integerField property should exist");

        assertNotNull(integerField.getExamples(), "examples array should exist");
        assertEquals(integerField.getExamples().size(), 3, "examples array should have 3 elements");

        assertTrue(integerField.getExamples().contains("1"), "examples should contain '1' as string");
        assertTrue(integerField.getExamples().contains(null), "examples should contain null value");
        assertTrue(integerField.getExamples().contains("100"), "examples should contain '100' as string");
    }

    /**
     * Test that examples array is NOT set in OAS 3.0 (only OAS 3.1)
     * 
     * Note: In OAS 3.0, the 'examples' field (plural, as array) is only valid on:
     * - Parameter Object (parameters[*].examples) - as Map<String, Example>
     * - Media Type Object (content[media-type].examples) - as Map<String, Example>
     * - Header Object (headers[*].examples) - as Map<String, Example>
     * 
     * For Schema objects, OAS 3.0 only supports the singular 'example' field.
     * The 'examples' array was added to Schema objects in OAS 3.1.
     */
    @Test
    public void testExamplesArrayNotSetInOAS30() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(false);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(NullableStringWithExamplesModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema stringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("stringField");
        assertNotNull(stringField, "stringField property should exist");
        assertEquals(stringField.getNullable(), Boolean.TRUE, "Field should be nullable");

        assertNull(stringField.getExamples(), "examples array should NOT be set in OAS 3.0");
    }

    /**
     * Test that when both example and examples are set, only examples is used in OAS 3.1
     * The single example field is ignored by design when examples array is present
     */
    @Test
    public void testExamplesArrayTakesPrecedenceOverExample_OAS31() throws Exception {
        final ModelResolver modelResolver = new ModelResolver(Json31.mapper());
        Configuration configuration = new Configuration();
        configuration.setOpenAPI31(true);
        modelResolver.setConfiguration(configuration);
        final ModelConverterContextImpl context = new ModelConverterContextImpl(modelResolver);

        final io.swagger.v3.oas.models.media.Schema model = context
                .resolve(new AnnotatedType(BothExampleAndExamplesModel.class));

        assertNotNull(model);
        assertNotNull(model.getProperties());

        io.swagger.v3.oas.models.media.Schema stringField =
                (io.swagger.v3.oas.models.media.Schema) model.getProperties().get("stringField");
        assertNotNull(stringField, "stringField property should exist");
        assertTrue(isNullableInOAS31(stringField), "Field should be nullable (type array should contain 'null')");

        assertNull(stringField.getExample(),
                "example field should be null/not set when examples array is present");
        assertNotNull(stringField.getExamples(), "examples array should be set");
        assertEquals(stringField.getExamples().size(), 3, "examples array should have 3 elements");

        assertTrue(stringField.getExamples().contains("arrayValue1"), "examples should contain 'arrayValue1'");
        assertTrue(stringField.getExamples().contains(null), "examples should contain null");
        assertTrue(stringField.getExamples().contains("arrayValue3"), "examples should contain 'arrayValue3'");

        assertFalse(stringField.getExamples().contains("singleExampleValue"),
                "examples should NOT contain the value from single example field");
    }

    private boolean isNullableInOAS31(io.swagger.v3.oas.models.media.Schema schema) {
        if (schema.getTypes() != null && schema.getTypes().contains("null")) {
            return true;
        }
        return false;
    }

    // Test model classes

    public static class NullableStringModel {
        @Schema(nullable = true, example = "null", defaultValue = "null")
        private String nullableStringField;

        public String getNullableStringField() {
            return nullableStringField;
        }

        public void setNullableStringField(String nullableStringField) {
            this.nullableStringField = nullableStringField;
        }
    }

    public static class NullableIntegerModel {
        @Schema(nullable = true, example = "null", defaultValue = "null")
        private Integer nullableIntegerField;

        public Integer getNullableIntegerField() {
            return nullableIntegerField;
        }

        public void setNullableIntegerField(Integer nullableIntegerField) {
            this.nullableIntegerField = nullableIntegerField;
        }
    }

    public static class NullableBigDecimalModel {
        @Schema(nullable = true, example = "null", defaultValue = "null")
        private BigDecimal nullableBigDecimalField;

        public BigDecimal getNullableBigDecimalField() {
            return nullableBigDecimalField;
        }

        public void setNullableBigDecimalField(BigDecimal nullableBigDecimalField) {
            this.nullableBigDecimalField = nullableBigDecimalField;
        }
    }

    public static class NullableBooleanModel {
        @Schema(nullable = true, example = "null", defaultValue = "null")
        private Boolean nullableBooleanField;

        public Boolean getNullableBooleanField() {
            return nullableBooleanField;
        }

        public void setNullableBooleanField(Boolean nullableBooleanField) {
            this.nullableBooleanField = nullableBooleanField;
        }
    }

    public static class NullableObjectModel {
        @Schema(nullable = true, example = "null", defaultValue = "null")
        private Object nullableObjectField;

        public Object getNullableObjectField() {
            return nullableObjectField;
        }

        public void setNullableObjectField(Object nullableObjectField) {
            this.nullableObjectField = nullableObjectField;
        }
    }


    public static class NullableWithoutExampleDefaultModel {
        @Schema(nullable = true)
        private String stringField;

        @Schema(nullable = true)
        private Integer integerField;

        @Schema(nullable = true)
        private Boolean booleanField;

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }

        public Integer getIntegerField() {
            return integerField;
        }

        public void setIntegerField(Integer integerField) {
            this.integerField = integerField;
        }

        public Boolean getBooleanField() {
            return booleanField;
        }

        public void setBooleanField(Boolean booleanField) {
            this.booleanField = booleanField;
        }
    }

    public static class NullableWithDefaultOnlyModel {
        @Schema(nullable = true, defaultValue = "defaultValue")
        private String stringField;

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }
    }

    public static class NullableWithExampleOnlyModel {
        @Schema(nullable = true, example = "exampleValue")
        private String stringField;

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }
    }


    // Test model classes for non-nullable fields with null example/default

    public static class NonNullableStringModel {
        @Schema(nullable = false, example = "null", defaultValue = "null")
        private String stringField;

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }
    }

    public static class NonNullableBooleanModel {
        @Schema(nullable = false, example = "null", defaultValue = "null")
        private Boolean booleanField;

        public Boolean getBooleanField() {
            return booleanField;
        }

        public void setBooleanField(Boolean booleanField) {
            this.booleanField = booleanField;
        }
    }

    public static class NonNullableIntegerModel {
        @Schema(nullable = false, example = "null", defaultValue = "null")
        private Integer integerField;

        public Integer getIntegerField() {
            return integerField;
        }

        public void setIntegerField(Integer integerField) {
            this.integerField = integerField;
        }
    }

    /**
     * Test model class for Issue #4229
     * Matches the exact example from the issue report
     */
    public static class Issue4229TestData {
        @Schema(nullable = true, example = "null")
        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    /**
     * Test model with examples array containing "null" string in OAS 3.1
     */
    public static class NullableStringWithExamplesModel {
        @Schema(nullable = true, examples = {"validValue", "null", "anotherValue"})
        private String stringField;

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }
    }

    /**
     * Test model with examples array containing multiple values including "null"
     */
    public static class NullableIntegerWithMultipleExamplesModel {
        @Schema(nullable = true, examples = {"1", "null", "100"})
        private Integer integerField;

        public Integer getIntegerField() {
            return integerField;
        }

        public void setIntegerField(Integer integerField) {
            this.integerField = integerField;
        }
    }


    /**
     * Test model with both example and examples set
     * Only examples should be used in the generated OpenAPI
     */
    public static class BothExampleAndExamplesModel {
        @Schema(nullable = true, example = "singleExampleValue", examples = {"arrayValue1", "null", "arrayValue3"})
        private String stringField;

        public String getStringField() {
            return stringField;
        }

        public void setStringField(String stringField) {
            this.stringField = stringField;
        }
    }
}
