package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Model;
import io.swagger.models.ModelContainingModelWithReference;
import io.swagger.models.ModelWithReference;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

public class ModelWithReferenceTest {

    @Test(description = "it should convert a model with reference property")
    public void convertModelWithReferenceProperty() throws IOException {
        final Map<String, Model> schemas = ModelConverters.getInstance().read(ModelWithReference.class);
        final String json = "{\n" +
                "   \"ModelWithReference\":{\n" +
                "      \"type\":\"object\",\n" +
                "      \"properties\":{\n" +
                "         \"description\":{\n" +
                "            \"$ref\":\"http://swagger.io/schemas.json#/Models/Description\"\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(schemas, json);
    }

    @Test(description = "it should convert a model with reference and reference property")
    public void convertModelWithReferenceAndReferenceProperty() throws IOException {
        final Map<String, Model> schemas = ModelConverters.getInstance().read(ModelContainingModelWithReference.class);
        final String json = "{\n" +
                "   \"ModelContainingModelWithReference\":{\n" +
                "      \"type\":\"object\",\n" +
                "      \"properties\":{\n" +
                "         \"model\":{\n" +
                "            \"$ref\":\"http://swagger.io/schemas.json#/Models\"\n" +
                "         },\n" +
                "         \"anotherModel\":{\n" +
                "            \"$ref\":\"http://swagger.io/schemas.json#/Models/AnotherModel\"\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(schemas, json);
    }
}
