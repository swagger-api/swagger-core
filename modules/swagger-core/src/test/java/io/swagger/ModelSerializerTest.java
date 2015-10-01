package io.swagger;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.ArrayModel;
import io.swagger.models.Car;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Manufacturers;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.properties.*;
import io.swagger.util.Json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ModelSerializerTest {
    private final ObjectMapper m = Json.mapper();

    @Test(description = "it should convert a model")
    public void convertModel() throws JsonProcessingException {
        final ModelImpl pet = new ModelImpl();
        final HashMap<String, Property> props = new HashMap<String, Property>();
        props.put("intValue", new IntegerProperty());
        props.put("longValue", new LongProperty());
        props.put("dateValue", new DateProperty());
        props.put("dateTimeValue", new DateTimeProperty());
        pet.setProperties(props);
        pet.setRequired(Arrays.asList("intValue", "name"));
        final String json = "{\n" +
                "   \"required\":[\n" +
                "      \"intValue\"\n" +
                "   ],\n" +
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
                "      }\n" +
                "   }\n" +
                "}";

        SerializationMatchers.assertEqualsToJson(pet, json);
    }

    @Test(description = "it should deserialize a model")
    public void deserializeModel() throws IOException {
        final String json = "{\n" +
                "   \"required\":[\n" +
                "      \"intValue\"\n" +
                "   ],\n" +
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

        final Model p = m.readValue(json, Model.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize an array model")
    public void serializeArrayModel() throws IOException {
        final ArrayModel model = new ArrayModel();
        model.setItems(new RefProperty("Pet"));
        assertEquals(m.writeValueAsString(model), "{\"type\":\"array\",\"items\":{\"$ref\":\"#/definitions/Pet\"}}");
    }

    @Test(description = "it should deserialize an array model")
    public void deserializeArrayModel() throws IOException {
        final String json = "{\"type\":\"array\",\"items\":{\"$ref\":\"#/definitions/Pet\"}}";
        final Model p = m.readValue(json, Model.class);
        assertTrue(p instanceof ArrayModel);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should not create an xml object for ref")
    public void shouldNotCreateXmlObjectForRef() throws IOException {
        final RefModel model = new RefModel("Monster");
        model.setDescription("oops");
        model.setExternalDocs(new ExternalDocs("external docs", "http://swagger.io"));
        assertEquals(Json.mapper().writeValueAsString(model), "{\"$ref\":\"#/definitions/Monster\"}");
    }

    @Test(description = "it should make a field readOnly by annotation")
    public void makeFieldReadOnly() throws IOException {
        final Map<String, Model> schemas = ModelConverters.getInstance().read(Car.class);
        final String json = "{\n" +
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
        final Map<String, Model> schemas = ModelConverters.getInstance().read(Manufacturers.class);
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

        final ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);
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
        final ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);
        Property property = model.getProperties().get("id");
        assertTrue(property.getReadOnly());
    }

    /*
    @Test(description = "it should deserialize a model with custom format")
    public void deserializeModelWithCustomFormat() throws IOException {
        final String json = "{\n" +
                "   \"properties\":{\n" +
                "      \"id\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"format\":\"custom\"\n" +
                "      }\n" +
                "   }\n" +
                "}";
        final ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);
        Json.prettyPrint(model);
    }*/

    @Test(description = "it should generate a JSON with read-only from pojo, #1161")
    public void readOnlyJsonGeneration() throws IOException {
        Map<String, Model> models = ModelConverters.getInstance().read(io.swagger.models.ReadOnlyModel.class);

        Model model = models.get("ReadOnlyModel");

        Property id = model.getProperties().get("id");
        assertTrue(id.getReadOnly());
        
        Property readWriteId = model.getProperties().get("readWriteId");
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
        final ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);
        IntegerProperty p = (IntegerProperty)model.getProperties().get("id");

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
        final ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);
        StringProperty p = (StringProperty)model.getProperties().get("AdvStateType");

        assertNotNull(p.getEnum());
        assertEquals(p.getEnum().get(0), "off");
        assertEquals(p.getEnum().get(1), "on");
    }
}