package io.swagger.v3.core.util;

import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.lang.annotation.Annotation;
import static org.testng.Assert.assertNotNull;


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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

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
@Test
    public void testExampleStartingWithNumberShouldBeString() throws Exception {
        io.swagger.v3.oas.annotations.media.Schema annotation = new io.swagger.v3.oas.annotations.media.Schema() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return io.swagger.v3.oas.annotations.media.Schema.class;
            }

            @Override public String name() { return ""; }
            @Override public String title() { return ""; }
            @Override public String description() { return ""; }
            @Override public String format() { return ""; }
            @Override public String ref() { return ""; }
            @Override public boolean nullable() { return false; }
            @Override public boolean required() { return false; }
            @Override public io.swagger.v3.oas.annotations.media.Schema.AccessMode accessMode() { return io.swagger.v3.oas.annotations.media.Schema.AccessMode.AUTO; }
            @Override public io.swagger.v3.oas.annotations.media.Schema.RequiredMode requiredMode() { return io.swagger.v3.oas.annotations.media.Schema.RequiredMode.AUTO; }
            @Override public String example() { return "5 lacs per annum"; }
            @Override public Class<?> implementation() { return java.lang.Void.class; }
            @Override public Class<?> not() { return java.lang.Void.class; }
            @Override public Class<?>[] oneOf() { return new Class<?>[0]; }
            @Override public Class<?>[] anyOf() { return new Class<?>[0]; }
            @Override public Class<?>[] allOf() { return new Class<?>[0]; }

            @Override public double multipleOf() { return 0; }
            @Override public String maximum() { return ""; }
            @Override public boolean exclusiveMaximum() { return false; }
            @Override public String minimum() { return ""; }
            @Override public boolean exclusiveMinimum() { return false; }
            @Override public int maxLength() { return Integer.MAX_VALUE; }
            @Override public int minLength() { return 0; }
            @Override public String pattern() { return ""; }
            @Override public int maxProperties() { return 0; }
            @Override public int minProperties() { return 0; }

            @Override public boolean hidden() { return false; }
            @Override public boolean enumAsRef() { return false; }
            @Override public boolean deprecated() { return false; }
            @Override public boolean readOnly() { return false; }
            @Override public boolean writeOnly() { return false; }
            @Override public String type() { return ""; }
            @Override public String defaultValue() { return ""; }
            @Override public String discriminatorProperty() { return ""; }
            @Override public String[] allowableValues() { return new String[0]; }

            @Override public String[] requiredProperties() { return new String[0]; }

            @Override public io.swagger.v3.oas.annotations.media.Schema.SchemaResolution schemaResolution() { return io.swagger.v3.oas.annotations.media.Schema.SchemaResolution.DEFAULT; }
            @Override public String _const() { return ""; }
            @Override public String[] examples() { return new String[0]; }
            @Override public Class<?> additionalPropertiesSchema() { return java.lang.Void.class; }
            @Override public Class<?> unevaluatedProperties() { return java.lang.Void.class; }
            @Override public io.swagger.v3.oas.annotations.StringToClassMapItem[] properties() { return new io.swagger.v3.oas.annotations.StringToClassMapItem[0]; }
            @Override public io.swagger.v3.oas.annotations.StringToClassMapItem[] patternProperties() { return new io.swagger.v3.oas.annotations.StringToClassMapItem[0]; }
            @Override public io.swagger.v3.oas.annotations.StringToClassMapItem[] dependentSchemas() { return new io.swagger.v3.oas.annotations.StringToClassMapItem[0]; }
            @Override public io.swagger.v3.oas.annotations.media.DependentRequired[] dependentRequiredMap() { return new io.swagger.v3.oas.annotations.media.DependentRequired[0]; }
            @Override public io.swagger.v3.oas.annotations.media.Schema.AdditionalPropertiesValue additionalProperties() { return io.swagger.v3.oas.annotations.media.Schema.AdditionalPropertiesValue.FALSE; }
            @Override public Class<?>[] exampleClasses() { return new Class<?>[0]; }
            @Override public String $comment() { return ""; }
            @Override public Class<?> then() { return java.lang.Void.class; }
            @Override public Class<?> _else() { return java.lang.Void.class; }
            @Override public Class<?> _if() { return java.lang.Void.class; }
            @Override public Class<?> unevaluatedItems() { return java.lang.Void.class; }
            @Override public Class<?> additionalItems() { return java.lang.Void.class; }
            @Override public int minContains() { return 0; }
            @Override public int maxContains() { return 0; }
            @Override public Class<?> propertyNames() { return java.lang.Void.class; }
            @Override public Class<?> contentSchema() { return java.lang.Void.class; }
            @Override public String contentMediaType() { return ""; }
            @Override public String contentEncoding() { return ""; }
            @Override public String $dynamicRef() { return ""; }
            @Override public String $dynamicAnchor() { return ""; }
            @Override public String $vocabulary() { return ""; }
            @Override public String $anchor() { return ""; }
            @Override public String $schema() { return ""; }
            @Override public String $id() { return ""; }
            @Override public Class<?> contains() { return java.lang.Void.class; }
            @Override public int exclusiveMinimumValue() { return 0; }
            @Override public int exclusiveMaximumValue() { return 0; }
            @Override public String[] types() { return new String[0]; }
            @Override public Class<?>[] prefixItems() { return new Class<?>[0]; }
            @Override public io.swagger.v3.oas.annotations.extensions.Extension[] extensions() { return new io.swagger.v3.oas.annotations.extensions.Extension[0]; }
            @Override public Class<?>[] subTypes() { return new Class<?>[0]; }
            @Override public io.swagger.v3.oas.annotations.media.DiscriminatorMapping[] discriminatorMapping() { return new io.swagger.v3.oas.annotations.media.DiscriminatorMapping[0]; }

            @Override
            public io.swagger.v3.oas.annotations.ExternalDocumentation externalDocs() {
                return new io.swagger.v3.oas.annotations.ExternalDocumentation() {
                    @Override public Class<? extends Annotation> annotationType() { return io.swagger.v3.oas.annotations.ExternalDocumentation.class; }
                    @Override public String description() { return ""; }
                    @Override public String url() { return ""; }
                    @Override public io.swagger.v3.oas.annotations.extensions.Extension[] extensions() { return new io.swagger.v3.oas.annotations.extensions.Extension[0]; }
                };
            }
        };

        io.swagger.v3.oas.models.media.Schema<?> schema =
                AnnotationsUtils.getSchemaFromAnnotation(annotation, null, null, false, null).get();

        assertNotNull(schema);
        assertEquals(schema.getExample(), "5 lacs per annum");
    }

    class DummyClass implements Serializable {}

}
