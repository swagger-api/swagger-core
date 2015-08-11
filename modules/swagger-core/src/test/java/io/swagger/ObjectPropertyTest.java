package io.swagger;

import static org.testng.Assert.assertTrue;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.*;
import io.swagger.models.properties.*;
import io.swagger.util.Json;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ObjectPropertyTest {
    @org.junit.Test //(description = "convert a model with object properties")
    public void readModelWithObjectProperty() throws IOException {
        String json = "{\n\"properties\": {\n\"id\": {\n\"type\": \"string\"\n},\n\"someObject\": {\n\"type\": \"object\",\n\"properties\": {\n\"innerId\": {\n\"type\": \"string\"\n}\n}\n}\n}\n}";

        ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);

        Property p = model.getProperties().get("someObject");
        assertTrue(p instanceof ObjectProperty);

        ObjectProperty op = (ObjectProperty) p;

        Property sp = op.getProperties().get("innerId");
        assertTrue(sp instanceof StringProperty);
    }
}
