package io.swagger.v3.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.DependentRequired;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNull;

public class AnnotationsUtilsTest {

    @DataProvider
    private Object[][] expectedSchemaFromTypes() {
        return new Object[][]{
                {String.class, ImmutableMap.of("type", "string")},
                {Character.class, ImmutableMap.of("type", "string")},
                {Boolean.class, ImmutableMap.of("type", "boolean")},
                {Byte.class, ImmutableMap.of("type", "string", "format", "byte")},
                {URI.class, ImmutableMap.of("type", "string", "format", "uri")},
                {URL.class, ImmutableMap.of("type", "string", "format", "url")},
                {UUID.class, ImmutableMap.of("type", "string", "format", "uuid")},
                {Short.class, ImmutableMap.of("type", "integer", "format", "int32")},
                {Integer.class, ImmutableMap.of("type", "integer", "format", "int32")},
                {Long.class, ImmutableMap.of("type", "integer", "format", "int64")},
                {Float.class, ImmutableMap.of("type", "number", "format", "float")},
                {Double.class, ImmutableMap.of("type", "number", "format", "double")},
                {BigInteger.class, ImmutableMap.of("type", "integer")},
                {BigDecimal.class, ImmutableMap.of("type", "number")},
                {Number.class, ImmutableMap.of("type", "number")},
                {Date.class, ImmutableMap.of("type", "string", "format", "date-time")},
                {File.class, ImmutableMap.of("type", "string", "format", "binary")},
                {Object.class, ImmutableMap.of("type", "object")},
                {DummyClass.class, ImmutableMap.of("$ref", "#/components/schemas/DummyClass")}
        };
    }

    @Test(dataProvider = "expectedSchemaFromTypes")
    public void resolveSchemaFromType(Class<?> aClass, Map<String, Object> expected) {
        Schema schema = AnnotationsUtils.resolveSchemaFromType(aClass, new Components(), null);

        assertEquals(schema.getType(), expected.get("type"));
        assertEquals(schema.getFormat(), expected.get("format"));
        assertEquals(schema.get$ref(), expected.get("$ref"));
    }

    @DataProvider
    private Object[][] expectedSchemaFromTypeAndFormat() {
        return new Object[][]{
                {"byteType", ImmutableMap.of("type", "string", "format", "byte")},
                {"binaryType", ImmutableMap.of("type", "string", "format", "binary")},
                {"emailType", ImmutableMap.of("type", "string", "format", "email")},
                {"dummyType", ImmutableMap.of("$ref", "#/components/schemas/DummyClass")}
        };
    }

    @Test(dataProvider = "expectedSchemaFromTypeAndFormat")
    public void getSchema(String methodName, Map<String, Object> expected) throws NoSuchMethodException {
        final Method method = getClass().getDeclaredMethod(methodName);
        Content annotationContent = method.getAnnotation(ApiResponse.class).content()[0];
        Optional<? extends Schema> schema = AnnotationsUtils.getSchema(annotationContent, new Components(), null, false);

        assertTrue(schema.isPresent());
        assertEquals(schema.get().getType(), expected.get("type"));
        assertEquals(schema.get().getFormat(), expected.get("format"));
        assertEquals(schema.get().get$ref(), expected.get("$ref"));
    }

    @ApiResponse(content = @Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Byte.class)))
    private void byteType() {
    }

    @ApiResponse(content = @Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Byte.class, format = "binary")))
    private void binaryType() {
    }

    @ApiResponse(content = @Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class, format = "email")))
    private void emailType() {
    }

    @ApiResponse(content = @Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DummyClass.class)))
    private void dummyType() {
    }

    class DummyClass implements Serializable {}

    static class ExampleHolder {
        @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "5 lacs per annum")
        String value;

        @io.swagger.v3.oas.annotations.media.Schema(type = "number", example = "10")
        String numberValue;

        @io.swagger.v3.oas.annotations.media.Schema(type = "integer", example = "5")
        String integerValue;

        @io.swagger.v3.oas.annotations.media.Schema(example = "10.00")
        BigDecimal bigDecimalValue;

        @io.swagger.v3.oas.annotations.media.Schema(example = "42", type = "string")
        String stringWith42;

        @io.swagger.v3.oas.annotations.media.Schema(defaultValue = "10.00")
        BigDecimal bigDecimalDefault;

        @io.swagger.v3.oas.annotations.media.Schema(defaultValue = "42", type = "string")
        String stringDefaultWith42;

        @io.swagger.v3.oas.annotations.media.Schema(examples = {"10.00", "20.50"})
        BigDecimal bigDecimalExamples;

        @io.swagger.v3.oas.annotations.media.Schema(examples = {"42"}, type = "string")
        String stringExamplesWith42;

        @io.swagger.v3.oas.annotations.media.Schema(example = "true")
        boolean aBoolean;

        @io.swagger.v3.oas.annotations.media.Schema(example = "true", type = "boolean")
        String stringWithBooleanType;

        @io.swagger.v3.oas.annotations.media.Schema(example = "true", type = "string")
        String stringWithBooleanExample;
    }

    @Test
    public void testExampleStartingWithNumberShouldBeString() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                ExampleHolder.class
                        .getDeclaredField("value")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        assertEquals(schema.get().getExample(), "5 lacs per annum");
    }

    @Test
    public void testExampleWithNumberTypeShouldHaveExampleAsNumber() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                ExampleHolder.class
                        .getDeclaredField("numberValue")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        assertEquals(schema.get().getExample(), IntNode.valueOf(10));
    }

    @Test
    public void testExampleWithIntegerTypeShouldHaveExampleAsNumber() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                ExampleHolder.class
                        .getDeclaredField("integerValue")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        assertEquals(schema.get().getExample(), IntNode.valueOf(5));
    }

    @Test
    public void testExampleBooleanShouldBeNode() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                ExampleHolder.class
                        .getDeclaredField("aBoolean")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        assertEquals(schema.get().getExample(), BooleanNode.getTrue());
    }

    @Test
    public void testExampleStringWithBooleanTypeShouldBeNode() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                ExampleHolder.class
                        .getDeclaredField("stringWithBooleanType")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        assertEquals(schema.get().getExample(), BooleanNode.getTrue());
    }

    @Test
    public void testExampleStringWithBooleanShouldBeString() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                ExampleHolder.class
                        .getDeclaredField("stringWithBooleanExample")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        assertEquals(schema.get().getExample(), "true");
    }

    static class DefaultHolder {

        @io.swagger.v3.oas.annotations.media.Schema(type = "string")
        String noDefault;

        @io.swagger.v3.oas.annotations.media.Schema(type = "string", defaultValue = "null")
        String nullDefaultValue;

        @io.swagger.v3.oas.annotations.media.Schema(type = "string", nullable = true, defaultValue = "null")
        String nullDefaultNullableValue;
    }

    @Test
    public void testNoDefaultSet() throws NoSuchFieldException {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                DefaultHolder.class
                        .getDeclaredField("noDefault")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        assertFalse(schema.get().getDefaultSetFlag());
        assertNull(schema.get().getDefault());
    }

    @Test
    public void testSetDefaultNullWithoutNullable() throws NoSuchFieldException {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                DefaultHolder.class
                        .getDeclaredField("nullDefaultValue")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        assertTrue(schema.get().getDefaultSetFlag());
        assertEquals(schema.get().getDefault(), "null");
    }

    @Test
    public void testSetDefaultNullWithNullable() throws NoSuchFieldException {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                DefaultHolder.class
                        .getDeclaredField("nullDefaultNullableValue")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        assertTrue(schema.get().getDefaultSetFlag());
        assertNull(schema.get().getDefault());
    }

    @Test
    public void testOverrideSoGetDefaultValueReturnsNull() throws NoSuchFieldException {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                DefaultHolder.class
                        .getDeclaredField("noDefault")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        io.swagger.v3.oas.annotations.media.Schema nullableDefaultSchema = new io.swagger.v3.oas.annotations.media.Schema() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return schemaAnnotation.annotationType();
            }

            @Override
            public Class<?> implementation() {
                return schemaAnnotation.implementation();
            }

            @Override
            public Class<?> not() {
                return schemaAnnotation.not();
            }

            @Override
            public Class<?>[] oneOf() {
                return schemaAnnotation.oneOf();
            }

            @Override
            public Class<?>[] anyOf() {
                return schemaAnnotation.anyOf();
            }

            @Override
            public Class<?>[] allOf() {
                return schemaAnnotation.allOf();
            }

            @Override
            public String name() {
                return schemaAnnotation.name();
            }

            @Override
            public String title() {
                return schemaAnnotation.title();
            }

            @Override
            public double multipleOf() {
                return schemaAnnotation.multipleOf();
            }

            @Override
            public String maximum() {
                return schemaAnnotation.maximum();
            }

            @Override
            public boolean exclusiveMaximum() {
                return schemaAnnotation.exclusiveMaximum();
            }

            @Override
            public String minimum() {
                return schemaAnnotation.minimum();
            }

            @Override
            public boolean exclusiveMinimum() {
                return schemaAnnotation.exclusiveMinimum();
            }

            @Override
            public int maxLength() {
                return schemaAnnotation.maxLength();
            }

            @Override
            public int minLength() {
                return schemaAnnotation.minLength();
            }

            @Override
            public String pattern() {
                return schemaAnnotation.pattern();
            }

            @Override
            public int maxProperties() {
                return schemaAnnotation.maxProperties();
            }

            @Override
            public int minProperties() {
                return schemaAnnotation.minProperties();
            }

            @Override
            public String[] requiredProperties() {
                return schemaAnnotation.requiredProperties();
            }

            @Override
            public boolean required() {
                return schemaAnnotation.required();
            }

            @Override
            public RequiredMode requiredMode() {
                return schemaAnnotation.requiredMode();
            }

            @Override
            public String description() {
                return schemaAnnotation.description();
            }

            @Override
            public String format() {
                return schemaAnnotation.format();
            }

            @Override
            public String ref() {
                return schemaAnnotation.ref();
            }

            @Override
            public boolean nullable() {
                return schemaAnnotation.nullable();
            }

            @Override
            public boolean readOnly() {
                return schemaAnnotation.readOnly();
            }

            @Override
            public boolean writeOnly() {
                return schemaAnnotation.writeOnly();
            }

            @Override
            public AccessMode accessMode() {
                return schemaAnnotation.accessMode();
            }

            @Override
            public String example() {
                return schemaAnnotation.example();
            }

            @Override
            public ExternalDocumentation externalDocs() {
                return schemaAnnotation.externalDocs();
            }

            @Override
            public boolean deprecated() {
                return schemaAnnotation.deprecated();
            }

            @Override
            public String type() {
                return schemaAnnotation.type();
            }

            @Override
            public String[] allowableValues() {
                return schemaAnnotation.allowableValues();
            }

            @Override
            public String defaultValue() {
                return null;
            }

            @Override
            public String discriminatorProperty() {
                return schemaAnnotation.discriminatorProperty();
            }

            @Override
            public DiscriminatorMapping[] discriminatorMapping() {
                return schemaAnnotation.discriminatorMapping();
            }

            @Override
            public boolean hidden() {
                return schemaAnnotation.hidden();
            }

            @Override
            public boolean enumAsRef() {
                return schemaAnnotation.enumAsRef();
            }

            @Override
            public Class<?>[] subTypes() {
                return schemaAnnotation.subTypes();
            }

            @Override
            public Extension[] extensions() {
                return schemaAnnotation.extensions();
            }

            @Override
            public Class<?>[] prefixItems() {
                return schemaAnnotation.prefixItems();
            }

            @Override
            public String[] types() {
                return schemaAnnotation.types();
            }

            @Override
            public int exclusiveMaximumValue() {
                return schemaAnnotation.exclusiveMaximumValue();
            }

            @Override
            public int exclusiveMinimumValue() {
                return schemaAnnotation.exclusiveMinimumValue();
            }

            @Override
            public Class<?> contains() {
                return schemaAnnotation.contains();
            }

            @Override
            public String $id() {
                return schemaAnnotation.$id();
            }

            @Override
            public String $schema() {
                return schemaAnnotation.$schema();
            }

            @Override
            public String $anchor() {
                return schemaAnnotation.$anchor();
            }

            @Override
            public String $vocabulary() {
                return schemaAnnotation.$vocabulary();
            }

            @Override
            public String $dynamicAnchor() {
                return schemaAnnotation.$dynamicAnchor();
            }

            @Override
            public String $dynamicRef() {
                return schemaAnnotation.$dynamicRef();
            }

            @Override
            public String contentEncoding() {
                return schemaAnnotation.contentEncoding();
            }

            @Override
            public String contentMediaType() {
                return schemaAnnotation.contentMediaType();
            }

            @Override
            public Class<?> contentSchema() {
                return schemaAnnotation.contentSchema();
            }

            @Override
            public Class<?> propertyNames() {
                return schemaAnnotation.propertyNames();
            }

            @Override
            public int maxContains() {
                return schemaAnnotation.maxContains();
            }

            @Override
            public int minContains() {
                return schemaAnnotation.minContains();
            }

            @Override
            public Class<?> additionalItems() {
                return schemaAnnotation.additionalItems();
            }

            @Override
            public Class<?> unevaluatedItems() {
                return schemaAnnotation.unevaluatedItems();
            }

            @Override
            public Class<?> _if() {
                return schemaAnnotation._if();
            }

            @Override
            public Class<?> _else() {
                return schemaAnnotation._else();
            }

            @Override
            public Class<?> then() {
                return schemaAnnotation.then();
            }

            @Override
            public String $comment() {
                return schemaAnnotation.$comment();
            }

            @Override
            public Class<?>[] exampleClasses() {
                return schemaAnnotation.exampleClasses();
            }

            @Override
            public AdditionalPropertiesValue additionalProperties() {
                return schemaAnnotation.additionalProperties();
            }

            @Override
            public DependentRequired[] dependentRequiredMap() {
                return schemaAnnotation.dependentRequiredMap();
            }

            @Override
            public StringToClassMapItem[] dependentSchemas() {
                return schemaAnnotation.dependentSchemas();
            }

            @Override
            public StringToClassMapItem[] patternProperties() {
                return schemaAnnotation.patternProperties();
            }

            @Override
            public StringToClassMapItem[] properties() {
                return schemaAnnotation.properties();
            }

            @Override
            public Class<?> unevaluatedProperties() {
                return schemaAnnotation.unevaluatedProperties();
            }

            @Override
            public Class<?> additionalPropertiesSchema() {
                return schemaAnnotation.additionalPropertiesSchema();
            }

            @Override
            public String[] examples() {
                return schemaAnnotation.examples();
            }

            @Override
            public String _const() {
                return schemaAnnotation._const();
            }

            @Override
            public SchemaResolution schemaResolution() {
                return schemaAnnotation.schemaResolution();
            }
        };

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        nullableDefaultSchema,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        assertFalse(schema.get().getDefaultSetFlag());
        assertNull(schema.get().getDefault());
    }

    @Test
    public void testExampleOnBigDecimalFieldWithoutExplicitTypeShouldBeNumber() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                ExampleHolder.class
                        .getDeclaredField("bigDecimalValue")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        Object example = schema.get().getExample();
        assertTrue(example instanceof JsonNode, "Example should be a JsonNode, not a plain String");
        assertTrue(((JsonNode) example).isNumber(), "Example should be a numeric node for a BigDecimal field");
    }

    @Test
    public void testExampleWithExplicitStringTypeShouldNotProduceNumericNode() throws Exception {
        // Regression guard for #4999: explicit type="string" must never yield a numeric node
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                ExampleHolder.class
                        .getDeclaredField("stringWith42")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        Object example = schema.get().getExample();
        assertFalse(example instanceof JsonNode && ((JsonNode) example).isNumber(),
                "Example must not be a numeric node when type is explicitly 'string'");
        assertEquals(example, "42", "Example should be the string \"42\"");
    }

    @Test
    public void testDefaultWithExplicitStringTypeShouldNotProduceNumericNode() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                ExampleHolder.class
                        .getDeclaredField("stringDefaultWith42")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        Optional<Schema> schema =
                AnnotationsUtils.getSchemaFromAnnotation(
                        schemaAnnotation,
                        null,
                        null,
                        false,
                        null,
                        Schema.SchemaResolution.DEFAULT,
                        null
                );

        assertTrue(schema.isPresent());
        Object defaultValue = schema.get().getDefault();
        assertFalse(defaultValue instanceof JsonNode && ((JsonNode) defaultValue).isNumber(),
                "Default must not be a numeric node when type is explicitly 'string'");
        assertEquals(defaultValue, "42", "Default should be the string \"42\"");
    }

    @Test
    public void testExamplesArrayWithExplicitStringTypeShouldNotProduceNumericNodes31() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
                ExampleHolder.class
                        .getDeclaredField("stringExamplesWith42")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        List<Object> examples = AnnotationsUtils.parseExamplesArray(schemaAnnotation);

        assertEquals(examples.size(), 1);
        assertFalse(examples.get(0) instanceof JsonNode && ((JsonNode) examples.get(0)).isNumber(),
                "Example must not be a numeric JsonNode");
        assertEquals(examples.get(0), "42", "Example should be the string \"42\"");
    }

    // --- mergeSchemaAnnotations defaultValue tests ---

    @io.swagger.v3.oas.annotations.media.Schema(description = "type-level description")
    static class TypeWithDescriptionOnly {}

    static class FieldHolderWithDefault {
        @io.swagger.v3.oas.annotations.media.Schema(defaultValue = "myDefault")
        TypeWithDescriptionOnly field;
    }

    static class FieldHolderWithEmptyDefault {
        @io.swagger.v3.oas.annotations.media.Schema(defaultValue = "")
        TypeWithDescriptionOnly field;
    }

    @io.swagger.v3.oas.annotations.media.Schema(defaultValue = "typeDefault")
    static class TypeWithDefault {}

    static class FieldHolderWithTypeDefault {
        @io.swagger.v3.oas.annotations.media.Schema(description = "field-level description")
        TypeWithDefault field;
    }

    @Test
    public void mergedDefaultValueShouldUsePatchWhenMasterHasSentinel() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema master =
                TypeWithDescriptionOnly.class.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        io.swagger.v3.oas.annotations.media.Schema patch =
                FieldHolderWithDefault.class.getDeclaredField("field")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        io.swagger.v3.oas.annotations.media.Schema merged =
                AnnotationsUtils.mergeSchemaAnnotations(master, patch);

        assertEquals(merged.defaultValue(), "myDefault",
                "When master has no defaultValue (sentinel) and patch has one, the patch value should win");
    }

    @Test
    public void mergedDefaultValueShouldUseMasterWhenMasterHasRealValue() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema master =
                TypeWithDefault.class.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        io.swagger.v3.oas.annotations.media.Schema patch =
                FieldHolderWithDefault.class.getDeclaredField("field")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        io.swagger.v3.oas.annotations.media.Schema merged =
                AnnotationsUtils.mergeSchemaAnnotations(master, patch);

        assertEquals(merged.defaultValue(), "typeDefault",
                "When master has a real defaultValue, it should take precedence over patch");
    }

    @Test
    public void mergedDefaultValueShouldReturnSentinelWhenBothHaveSentinel() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema master =
                TypeWithDescriptionOnly.class.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        io.swagger.v3.oas.annotations.media.Schema patch =
                FieldHolderWithTypeDefault.class.getDeclaredField("field")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        io.swagger.v3.oas.annotations.media.Schema merged =
                AnnotationsUtils.mergeSchemaAnnotations(master, patch);

        assertEquals(merged.defaultValue(), "",
                "When both master and patch are empty, the merged result should also be empty");
    }

    @Test
    public void mergedDefaultValueShouldUseEmptyStringFromPatch() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema master =
                TypeWithDescriptionOnly.class.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        io.swagger.v3.oas.annotations.media.Schema patch =
                FieldHolderWithEmptyDefault.class.getDeclaredField("field")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        io.swagger.v3.oas.annotations.media.Schema merged =
                AnnotationsUtils.mergeSchemaAnnotations(master, patch);

        assertEquals(merged.defaultValue(), "",
                "When master has sentinel and patch has empty string, empty string should win");
    }

    @Test
    public void sentinelShouldNeverAppearInResolvedSchema() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema master =
                TypeWithDescriptionOnly.class.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        io.swagger.v3.oas.annotations.media.Schema patch =
                FieldHolderWithDefault.class.getDeclaredField("field")
                        .getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);

        io.swagger.v3.oas.annotations.media.Schema merged =
                AnnotationsUtils.mergeSchemaAnnotations(master, patch);

        assertNotEquals(merged.defaultValue(), "",
                "Sentinel should never be the result when patch has a real defaultValue");

        Optional<Schema> resolvedSchema = AnnotationsUtils.getSchemaFromAnnotation(
                merged, null, null, false, null, Schema.SchemaResolution.DEFAULT, null);

        assertTrue(resolvedSchema.isPresent());
        assertNotEquals(resolvedSchema.get().getDefault(), "",
                "Sentinel value must never appear in resolved schema default");
    }

}
