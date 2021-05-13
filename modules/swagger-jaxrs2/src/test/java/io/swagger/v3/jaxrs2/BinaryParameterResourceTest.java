package io.swagger.v3.jaxrs2;

import java.io.IOException;

import org.testng.annotations.Test;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.resources.BinaryParameterResource;

public class BinaryParameterResourceTest extends AbstractAnnotationTest {
    @Test(description = "check binary model serialization") // tests issue #2466
    public void shouldSerializeBinaryParameter() throws IOException {
        compareAsYaml(BinaryParameterResource.class, getOpenAPIAsString("BinaryParameterResource.yaml"));
    }
}
