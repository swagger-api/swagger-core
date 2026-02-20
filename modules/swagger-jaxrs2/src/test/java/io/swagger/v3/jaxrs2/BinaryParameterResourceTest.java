package io.swagger.v3.jaxrs2;

import java.io.IOException;

import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.resources.BinaryParameterResource;

public class BinaryParameterResourceTest extends AbstractAnnotationTest {

    @Test(description = "check binary model serialization with base64", singleThreaded = true) // tests issue #2466
    public void shouldSerializeBinaryParameterBase64() throws IOException {
        try {
            System.setProperty(Schema.BINARY_STRING_CONVERSION_PROPERTY, Schema.BynaryStringConversion.BINARY_STRING_CONVERSION_BASE64.toString());
            compareAsYaml(BinaryParameterResource.class, getOpenAPIAsString("BinaryParameterResource.yaml"));
        } finally {
            System.clearProperty(Schema.BINARY_STRING_CONVERSION_PROPERTY);
        }
    }

    @BeforeTest
    public void before() {
        System.clearProperty(Schema.BINARY_STRING_CONVERSION_PROPERTY);
    }

    @AfterTest
    public void after() {
        System.clearProperty(Schema.BINARY_STRING_CONVERSION_PROPERTY);
    }


    @Test(description = "check binary model serialization with StringSchema", singleThreaded = true) // tests issue #2466
    public void shouldSerializeBinaryParameterStringSchema() throws IOException {
        try {
            System.setProperty(Schema.BINARY_STRING_CONVERSION_PROPERTY, Schema.BynaryStringConversion.BINARY_STRING_CONVERSION_STRING_SCHEMA.toString());
            compareAsYaml(BinaryParameterResource.class, getOpenAPIAsString("BinaryParameterResource.yaml"));
        } finally {
            System.clearProperty(Schema.BINARY_STRING_CONVERSION_PROPERTY);
        }
    }
}
