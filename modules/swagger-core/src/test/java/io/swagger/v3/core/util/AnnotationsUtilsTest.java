package io.swagger.v3.core.util;

import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.Assert;
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

        Assert.assertEquals(schema.getType(), expected.get("type"));
        Assert.assertEquals(schema.getFormat(), expected.get("format"));
        Assert.assertEquals(schema.get$ref(), expected.get("$ref"));
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
        Optional<? extends Schema> schema = AnnotationsUtils.getSchema(annotationContent, new Components(), null);

        Assert.assertTrue(schema.isPresent());
        Assert.assertEquals(schema.get().getType(), expected.get("type"));
        Assert.assertEquals(schema.get().getFormat(), expected.get("format"));
        Assert.assertEquals(schema.get().get$ref(), expected.get("$ref"));
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

}