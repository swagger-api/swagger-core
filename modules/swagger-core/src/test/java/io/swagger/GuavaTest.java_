package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.GuavaModel;
import io.swagger.models.Model;
import io.swagger.util.ResourceUtils;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class GuavaTest {

    @Test(description = "convert a model with Guava optionals")
    public void convertModelWithGuavaOptionals() throws IOException {
        final Map<String, Model> schemas = ModelConverters.getInstance().read(GuavaModel.class);
        final String json = ResourceUtils.loadClassResource(getClass(), "GuavaTestModel.json");
        SerializationMatchers.assertEqualsToJson(schemas, json);
    }
}
