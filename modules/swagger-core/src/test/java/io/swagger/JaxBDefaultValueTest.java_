package io.swagger;

import static org.testng.Assert.assertEquals;

import io.swagger.converter.ModelConverters;
import io.swagger.models.Model;
import io.swagger.models.ModelWithJaxBDefaultValues;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

import org.testng.annotations.Test;

import java.util.Map;

public class JaxBDefaultValueTest {

    @Test(description = "convert a model with Guava optionals")
    public void convertModelWithGuavaOptionals() {
        final Map<String, Model> schemas = ModelConverters.getInstance().read(ModelWithJaxBDefaultValues.class);
        final Map<String, Property> properties = schemas.get("ModelWithJaxBDefaultValues").getProperties();
        assertEquals(properties.size(), 2);
        assertEquals(((StringProperty) properties.get("name")).getDefault(), "Tony");
        assertEquals((int) ((IntegerProperty) properties.get("age")).getDefault(), 100);
    }
}
