package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Model;

import io.swagger.models.ModelImpl;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BinaryProperty;
import io.swagger.models.properties.ByteArrayProperty;
import io.swagger.util.Json;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class ByteConverterTest {

    @Test
    public void testByte() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ByteConverterModel.class);
        final String json = "{" +
                "   \"ByteConverterModel\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"myBytes\":{" +
                "            \"type\":\"array\"," +
                "            \"items\":{" +
                "               \"type\":\"string\"," +
                "               \"format\":\"byte\"" +
                "            }" +
                "         }" +
                "      }" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(models, json);
    }

    @Test
    public void testByteProperty() {
        Model model = new ModelImpl()
                .property("byteProperty", new ByteArrayProperty());

        assertEquals(Json.pretty(model), "{\n" +
                "  \"properties\" : {\n" +
                "    \"byteProperty\" : {\n" +
                "      \"type\" : \"string\",\n" +
                "      \"format\" : \"byte\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void testDeserializeByteProperty() throws Exception {
        String json = "{\n" +
                "  \"properties\" : {\n" +
                "    \"byteProperty\" : {\n" +
                "      \"type\" : \"string\",\n" +
                "      \"format\" : \"byte\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        Model model = Json.mapper().readValue(json, Model.class);
        Json.prettyPrint(model);
    }

    @Test
    public void testByteArray() {
        Model model = new ModelImpl()
                .property("byteArray", new ArrayProperty(new BinaryProperty()));

        assertEquals(Json.pretty(model), "{\n" +
                "  \"properties\" : {\n" +
                "    \"byteArray\" : {\n" +
                "      \"type\" : \"array\",\n" +
                "      \"items\" : {\n" +
                "        \"type\" : \"string\",\n" +
                "        \"format\" : \"binary\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }

    class ByteConverterModel {
        public Byte[] myBytes = new Byte[0];
    }
}
