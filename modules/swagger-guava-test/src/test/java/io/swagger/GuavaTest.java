package io.swagger;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.converter.ModelConverters;
import io.swagger.models.GuavaModel;
import io.swagger.models.Model;
import io.swagger.util.Json;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class GuavaTest {

    @Test(description = "convert a model with Guava optionals")
    public void convertModelWithGuavaOptionals() throws IOException {
        final Map<String, Model> schemas = ModelConverters.getInstance().read(GuavaModel.class);
        assertEquals(
            Json.mapper().convertValue(schemas, ObjectNode.class),
            Json.mapper().readValue(
                "{\n" +
                "    \"GuavaModel\": {\n" +
                "        \"type\": \"object\",\n" +
                "        \"properties\": {\n" +
                "            \"name\": {\n" +
                "                \"type\": \"string\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}\n", ObjectNode.class));
    }
}
