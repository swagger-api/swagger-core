package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.jaxrs.Reader;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Pet;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.resources.ResourceWithReferences;
import io.swagger.util.ResourceUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class ReferenceTest {

    @Test(description = "Scan a model with common reference and reference with ApiModel")
    public void scanModel() {
        final Map<String, Property> props = ModelConverters.getInstance().readAll(Pet.class).get("Pet").getProperties();
        final RefProperty category = (RefProperty) props.get("category");
        assertEquals(category.getType(), "ref");
        assertEquals(category.get$ref(), "#/definitions/Category");

        final RefProperty categoryWithApiModel = (RefProperty) props.get("categoryWithApiModel");
        assertEquals(categoryWithApiModel.getType(), "ref");
        assertEquals(categoryWithApiModel.get$ref(), "#/definitions/MyCategory");
    }

    @Test(description = "Scan API with operation and response references")
    public void scanAPI() throws IOException {
        final Swagger swagger = new Reader(new Swagger()).read(ResourceWithReferences.class);
        final String json = ResourceUtils.loadClassResource(getClass(), "ResourceWithReferences.json");
        SerializationMatchers.assertEqualsToJson(swagger, json);
    }
}
