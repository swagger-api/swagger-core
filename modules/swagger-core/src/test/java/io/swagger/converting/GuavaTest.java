package io.swagger.converting;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.oas.models.GuavaModel;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.util.ResourceUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class GuavaTest {

    @Test(description = "convert a model with Guava optionals")
    public void convertModelWithGuavaOptionals() throws IOException {
        final Map<String, Schema> schemas = ModelConverters.getInstance().read(GuavaModel.class);
        final String json = ResourceUtils.loadClassResource(getClass(), "GuavaTestModel.json");
        SerializationMatchers.assertEqualsToJson(schemas, json);
    }
}
