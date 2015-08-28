package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Model;

import org.testng.annotations.Test;

import java.util.Map;

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

    class ByteConverterModel {
        public Byte[] myBytes = new Byte[0];
    }
}
