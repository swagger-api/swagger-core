package io.swagger.v3.core.util;

import com.google.common.collect.ImmutableMap;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.Map;
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

    class DummyClass implements Serializable {}

}