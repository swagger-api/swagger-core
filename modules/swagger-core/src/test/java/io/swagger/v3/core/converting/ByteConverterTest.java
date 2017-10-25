package io.swagger.v3.core.converting;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BinarySchema;
import io.swagger.v3.oas.models.media.ByteArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ByteConverterTest {
    private static final String NEWLINE = System.getProperty("line.separator");
    
    @Test
    public void testByte() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ByteConverterModel.class);
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
        Schema model = new Schema()
                .addProperties("byteProperty", new ByteArraySchema());

        assertEquals(Json.pretty(model), "{" + NEWLINE +
                "  \"properties\" : {" + NEWLINE +
                "    \"byteProperty\" : {" + NEWLINE +
                "      \"type\" : \"string\"," + NEWLINE +
                "      \"format\" : \"byte\"" + NEWLINE +
                "    }" + NEWLINE +
                "  }" + NEWLINE +
                "}");
    }

    @Test
    public void testDeserializeByteProperty() throws Exception {
        String json =
                "{\n" +
                "  \"properties\" : {\n" +
                "    \"byteProperty\" : {\n" +
                "      \"type\" : \"string\",\n" +
                "      \"format\" : \"byte\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        Schema model = Json.mapper().readValue(json, Schema.class);
        assertNotNull(model);
    }

    @Test
    public void testByteArray() {
        Schema model = new Schema()
                .addProperties("byteArray", new ArraySchema().items(new BinarySchema()));

        assertEquals(Json.pretty(model), "{" + NEWLINE +
                "  \"properties\" : {" + NEWLINE +
                "    \"byteArray\" : {" + NEWLINE +
                "      \"type\" : \"array\"," + NEWLINE +
                "      \"items\" : {" + NEWLINE +
                "        \"type\" : \"string\"," + NEWLINE +
                "        \"format\" : \"binary\"" + NEWLINE +
                "      }" + NEWLINE +
                "    }" + NEWLINE +
                "  }" + NEWLINE +
                "}");
    }

    @Test
    public void testReadOnlyByteArray() {
        Schema model = new Schema()
                .addProperties("byteArray",
                        new ArraySchema().items(new BinarySchema()).readOnly(true));

        assertEquals(Json.pretty(model), "{" + NEWLINE +
                "  \"properties\" : {" + NEWLINE +
                "    \"byteArray\" : {" + NEWLINE +
                "      \"type\" : \"array\"," + NEWLINE +
                "      \"readOnly\" : true," + NEWLINE +
                "      \"items\" : {" + NEWLINE +
                "        \"type\" : \"string\"," + NEWLINE +
                "        \"format\" : \"binary\"" + NEWLINE +
                "      }" + NEWLINE +
                "    }" + NEWLINE +
                "  }" + NEWLINE +
                "}");
    }

    class ByteConverterModel {
        public Byte[] myBytes = new Byte[0];
    }
}
