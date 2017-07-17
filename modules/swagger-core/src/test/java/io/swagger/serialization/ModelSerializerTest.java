package io.swagger.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.oas.models.Car;
import io.swagger.oas.models.ExternalDocumentation;
import io.swagger.oas.models.Manufacturers;
import io.swagger.oas.models.ReadOnlyModel;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.DateSchema;
import io.swagger.oas.models.media.DateTimeSchema;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import io.swagger.util.Json;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class ModelSerializerTest {
    private final ObjectMapper m = Json.mapper();

    @Test(description = "it should convert a model")
    public void convertModel() throws JsonProcessingException {
        final Schema pet = new Schema();
        final Map<String, Schema> props = new LinkedHashMap<String, Schema>();
        props.put("intValue", new IntegerSchema());
        props.put("longValue", new IntegerSchema().format("int64"));
        props.put("dateValue", new DateSchema());
        props.put("dateTimeValue", new DateTimeSchema());
        pet.setProperties(props);
        pet.setRequired(Arrays.asList("intValue", "name"));
        final String json = "{\n" +
                "   \"required\":[\n" +
                "      \"intValue\"\n" +
                "   ],\n" +
                "   \"properties\":{\n" +
                "      \"intValue\":{\n" +
                "         \"type\":\"integer\",\n" +
                "         \"format\":\"int32\"\n" +
                "      },\n" +
                "      \"longValue\":{\n" +
                "         \"type\":\"integer\",\n" +
                "         \"format\":\"int64\"\n" +
                "      },\n" +
                "      \"dateValue\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"format\":\"date\"\n" +
                "      },\n" +
                "      \"dateTimeValue\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"format\":\"date-time\"\n" +
                "      }\n" +
                "   }\n" +
                "}";

        SerializationMatchers.assertEqualsToJson(pet, json);
    }

    @Test(description = "it should deserialize a model")
    public void deserializeModel() throws IOException {
        final String json =
                "{\n" +
                "   \"required\":[\n" +
                "      \"intValue\"\n" +
                "   ],\n" +
                "   \"type\":\"object\",\n" +
                "   \"properties\":{\n" +
                "      \"dateValue\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"format\":\"date\"\n" +
                "      },\n" +
                "      \"longValue\":{\n" +
                "         \"type\":\"integer\",\n" +
                "         \"format\":\"int64\"\n" +
                "      },\n" +
                "      \"dateTimeValue\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"format\":\"date-time\"\n" +
                "      },\n" +
                "      \"intValue\":{\n" +
                "         \"type\":\"integer\",\n" +
                "         \"format\":\"int32\"\n" +
                "      },\n" +
                "      \"byteArrayValue\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"format\":\"binary\"\n" +
                "      }\n" +
                "   }\n" +
                "}";

        final Schema p = m.readValue(json, Schema.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize an array model")
    public void serializeArrayModel() throws IOException {
        final ArraySchema model = new ArraySchema();
        model.setItems(new Schema().$ref("Pet"));
        assertEquals(m.writeValueAsString(model), "{\"type\":\"array\",\"items\":{\"$ref\":\"#/components/schemas/Pet\"}}");
    }

    @Test(description = "it should deserialize an array model")
    public void deserializeArrayModel() throws IOException {
        final String json = "{\"type\":\"array\",\"items\":{\"$ref\":\"#/definitions/Pet\"}}";
        final Schema p = m.readValue(json, Schema.class);
        assertTrue(p instanceof ArraySchema);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should not create an xml object for $ref")
    public void shouldNotCreateXmlObjectForRef() throws IOException {
        final Schema model = new Schema().$ref("Monster");
        model.setDescription("oops");
        model.setExternalDocs(new ExternalDocumentation()
                .description("external docs")
                .url("http://swagger.io"));
        assertEquals(Json.mapper().writeValueAsString(model), "{\"$ref\":\"#/components/schemas/Monster\"}");
    }

    @Test(description = "it should make a field readOnly by annotation")
    public void makeFieldReadOnly() throws IOException {
        final Map<String, Schema> schemas = ModelConverters.getInstance().read(Car.class);
        final String json =
                "{\n" +
                "   \"Car\":{\n" +
                "      \"type\":\"object\",\n" +
                "      \"properties\":{\n" +
                "         \"wheelCount\":{\n" +
                "            \"type\":\"integer\",\n" +
                "            \"format\":\"int32\",\n" +
                "            \"readOnly\":true\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(schemas, json);
    }

    @Test(description = "it should serialize a model with a Set")
    public void serializeModelWithSet() throws IOException {
        final Map<String, Schema> schemas = ModelConverters.getInstance().read(Manufacturers.class);
        final String json = "{\n" +
                "   \"Manufacturers\":{\n" +
                "      \"type\":\"object\",\n" +
                "      \"properties\":{\n" +
                "         \"countries\":{\n" +
                "            \"type\":\"array\",\n" +
                "            \"uniqueItems\":true,\n" +
                "            \"items\":{\n" +
                "               \"type\":\"string\"\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(schemas, json);
    }

    @Test(description = "it should deserialize a model with object example")
    public void deserializeModelWithObjectExample() throws IOException {
        final String json = "{\n" +
                "   \"title\":\"Error\",\n" +
                "   \"type\":\"object\",\n" +
                "   \"properties\":{\n" +
                "      \"code\":{\n" +
                "         \"type\":\"integer\",\n" +
                "         \"format\":\"int32\"\n" +
                "      },\n" +
                "      \"message\":{\n" +
                "         \"type\":\"string\"\n" +
                "      },\n" +
                "      \"fields\":{\n" +
                "         \"type\":\"string\"\n" +
                "      }\n" +
                "   },\n" +
                "   \"example\":{\n" +
                "      \"code\":1,\n" +
                "      \"message\":\"hello\",\n" +
                "      \"fields\":\"abc\"\n" +
                "   }\n" +
                "}";

        final Schema model = Json.mapper().readValue(json, Schema.class);
        assertEquals(Json.mapper().writeValueAsString(model.getExample()), "{\"code\":1,\"message\":\"hello\",\"fields\":\"abc\"}");
    }

    @Test(description = "it should deserialize a model with read-only property")
    public void deserializeModelWithReadOnlyProperty() throws IOException {
        final String json = "{\n" +
                "   \"properties\":{\n" +
                "      \"id\":{\n" +
                "         \"type\":\"integer\",\n" +
                "         \"format\":\"int32\",\n" +
                "         \"readOnly\":true\n" +
                "      }\n" +
                "   }\n" +
                "}";
        final Schema model = Json.mapper().readValue(json, Schema.class);
        Schema property = (Schema)model.getProperties().get("id");
        assertTrue(property.getReadOnly());
    }

    @Test(description = "it should generate a JSON with read-only from pojo, #1161")
    public void readOnlyJsonGeneration() throws IOException {
        Map<String, Schema> models = ModelConverters.getInstance().read(ReadOnlyModel.class);

        Schema model = models.get("ReadOnlyModel");

        Schema id = (Schema)model.getProperties().get("id");
        assertTrue(id.getReadOnly());

        Schema readWriteId = (Schema)model.getProperties().get("readWriteId");
        assertNull(readWriteId.getReadOnly());
    }

    @Test(description = "it should generate an integer field with enum")
    public void integerEnumGeneration() throws IOException {
            final String json = "{\n" +
                "   \"properties\":{\n" +
                "      \"id\":{\n" +
                "         \"description\":\"fun!\",\n" +
                "         \"type\":\"integer\",\n" +
                "         \"format\":\"int32\",\n" +
                "         \"readOnly\":true,\n" +
                "         \"enum\": [ 0, 1]\n" +
                "      }\n" +
                "   }\n" +
                "}";
        final Schema model = Json.mapper().readValue(json, Schema.class);
        IntegerSchema p = (IntegerSchema)model.getProperties().get("id");

        assertNotNull(p.getEnum());
        assertEquals(p.getEnum().get(0), new Integer(0));
        assertEquals(p.getEnum().get(1), new Integer(1));
    }

    @Test(description = "it retains enums per ")
    public void testEnumParser() throws IOException {
        String json = "{\n" +
                "  \"properties\": {\n" +
                "    \"AdvStateType\": {\n" +
                "      \"description\": \"Advertising State\",\n" +
                "      \"enum\": [\n" +
                "        \"off\",\n" +
                "        \"on\"\n" +
                "      ],\n" +
                "      \"type\": \"string\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        final Schema model = Json.mapper().readValue(json, Schema.class);
        StringSchema p = (StringSchema)model.getProperties().get("AdvStateType");

        assertNotNull(p.getEnum());
        assertEquals(p.getEnum().get(0), "off");
        assertEquals(p.getEnum().get(1), "on");
    }

    @Test
    public void testPrimitiveModel() throws Exception {
        String json = "{\n" +
                "  \"type\": \"string\",\n" +
                "  \"enum\": [\n" +
                "    \"a\",\n" +
                "    \"b\",\n" +
                "    \"c\"\n" +
                "  ]\n" +
                "}";

        final Schema model = Json.mapper().readValue(json, Schema.class);

        assertNotNull(model.getEnum());
        assertTrue(model.getEnum().size() == 3);
    }

    @Test
    public void testIssue1852() throws Exception {
        String json = "{\n" +
            "  \"type\": \"integer\",\n" +
            "  \"minimum\": 10,\n" +
            "  \"maximum\": 20,\n" +
            "  \"default\": 15\n" +
            "}";

        final Schema model = Json.mapper().readValue(json, Schema.class);

        assertEquals(model.getMinimum().intValue(), 10);
        assertEquals(model.getMaximum().intValue(), 20);
        assertEquals(model.getDefault(), 15);
    }

    @Test
    public void testIssue2064Neg() throws Exception {
        String json =
            "{\n" +
            "  \"type\": \"string\",\n" +
            "  \"uniqueItems\": false\n" +
            "}";

        final Schema model = Json.mapper().readValue(json, Schema.class);

        assertFalse(model.getUniqueItems());
    }

    @Test
    public void testIssue2064() throws Exception {
        String json =
            "{\n" +
            "  \"type\": \"string\",\n" +
            "  \"uniqueItems\": true\n" +
            "}";

        final Schema model = Json.mapper().readValue(json, Schema.class);

        assertTrue(model.getUniqueItems());
    }

    @Test
    public void testIssue2064Ip() throws Exception {
        String json =
            "{\n" +
            "  \"type\": \"object\",\n" +
            "  \"properties\": {\n" +
            "    \"id\": {\n" +
            "      \"type\": \"integer\",\n" +
            "      \"format\": \"int32\",\n" +
            "      \"multipleOf\": 3.0\n" +
            "    }\n" +
            "  }\n" +
            "}";

        final Schema model = Json.mapper().readValue(json, Schema.class);

        IntegerSchema ip = (IntegerSchema) model.getProperties().get("id");
        assertEquals(ip.getMultipleOf(), new BigDecimal("3.0"));

    }
}