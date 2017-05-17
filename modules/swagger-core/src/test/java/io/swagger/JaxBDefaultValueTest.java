package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.oas.models.ModelWithJaxBDefaultValues;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class JaxBDefaultValueTest {

    @Test(enabled = false, description = "convert a model with Guava optionals")
    public void convertModelWithGuavaOptionals() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().read(ModelWithJaxBDefaultValues.class);
        final Map<String, Schema> properties = schemas.get("ModelWithJaxBDefaultValues").getProperties();
        assertEquals(properties.size(), 2);
        assertEquals(((StringSchema) properties.get("name")).getDefault(), "Tony");
        assertEquals((int) ((IntegerSchema) properties.get("age")).getDefault(), 100);
    }
}
