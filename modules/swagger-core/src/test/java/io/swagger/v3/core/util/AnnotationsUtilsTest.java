package io.swagger.v3.core.util;

import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.DEFAULT_SENTINEL;
import static org.testng.Assert.*;

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

        assertEquals(merged.defaultValue(), DEFAULT_SENTINEL,
                "When both master and patch have sentinel, the merged result should also have sentinel");
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

        assertNotEquals(merged.defaultValue(), DEFAULT_SENTINEL,
                "Sentinel should never be the result when patch has a real defaultValue");

        Optional<Schema> resolvedSchema = AnnotationsUtils.getSchemaFromAnnotation(
                merged, null, null, false, null, Schema.SchemaResolution.DEFAULT, null);

        assertTrue(resolvedSchema.isPresent());
        assertNotEquals(resolvedSchema.get().getDefault(), DEFAULT_SENTINEL,
                "Sentinel value must never appear in resolved schema default");
    }

}
