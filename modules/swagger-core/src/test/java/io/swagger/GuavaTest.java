package io.swagger;

import static org.testng.Assert.assertTrue;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.GuavaModel;
import io.swagger.models.Model;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class GuavaTest {

    @Test(description = "convert a model with Guava optionals")
    public void convertModelWithGuavaOptionals() throws IOException {
        final Map<String, Model> schemas = ModelConverters.getInstance().read(GuavaModel.class);
        final InputStream in = getClass().getClassLoader().getResourceAsStream("GuavaTestModel.json");
        assertTrue(SerializationMatchers.compareAsJson(schemas, IOUtils.toString(in, StandardCharsets.UTF_8)));
    }
}
