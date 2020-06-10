package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.oas.models.GuavaModel;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.oas.models.media.Schema;
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
