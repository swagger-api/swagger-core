package io.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.ArrayModel;
import io.swagger.models.Car;
import io.swagger.models.ComposedModel;
import io.swagger.models.ExternalDocs;
import io.swagger.models.Manufacturers;
import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.RefModel;
import io.swagger.models.Xml;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
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

    @Test(description = "it should serialize an array model with xml")
    public void serializeArrayModelWithXml() throws IOException {
        final ArrayModel model = new ArrayModel();
        model.setItems(new RefProperty("Pet"));
        model.setXml(new Xml().wrapped(true).name("payments"));
        assertEquals(m.writeValueAsString(model), "{\"xml\":{\"name\":\"payments\",\"wrapped\":true},\"type\":\"array\",\"items\":{\"$ref\":\"#/definitions/Pet\"}}");
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

    @Test(description = "it should generate a JSON with read-only from pojo with accessMode annotation field, #2379")
    public void readOnlyAccessModeJsonGeneration() throws IOException {
        Map<String, Model> models = ModelConverters.getInstance().read(io.swagger.models.ReadOnlyModelUpdated.class);

        Model model = models.get("ReadOnlyModelUpdated");

        Property id = model.getProperties().get("id");
        assertTrue(id.getReadOnly());

        Property readWriteId = model.getProperties().get("readWriteId");
        assertNull(readWriteId.getReadOnly());

        Property autoId = model.getProperties().get("autoId");
        assertTrue(autoId.getReadOnly());
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

        final ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);

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

        final ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);

        assertEquals(model.getMinimum().intValue(), 10);
        assertEquals(model.getMaximum().intValue(), 20);
        assertEquals(model.getDefaultValue(), 15);
    }

    @Test
    public void testIssue2064Neg() throws Exception {
        String json = "{\n" +
            "  \"type\": \"string\",\n" +
            "  \"uniqueItems\": false\n" +
            "}";

        final ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);

        assertFalse(model.getUniqueItems());
    }

    @Test
    public void testIssue2064() throws Exception {
        String json = "{\n" +
            "  \"type\": \"string\",\n" +
            "  \"uniqueItems\": true\n" +
            "}";

        final ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);

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

        final ModelImpl model = Json.mapper().readValue(json, ModelImpl.class);

        IntegerProperty ip = (IntegerProperty) model.getProperties().get("id");
        assertEquals(ip.getMultipleOf(), new BigDecimal("3.0"));

    }

    @Test(description = "It should serialize a ModelImpl with new values added in 673")
    public void issue673SerializeModelImpl() throws IOException {
        final ModelImpl modelImpl = new ModelImpl();
        modelImpl.setMultipleOf(new BigDecimal(10));
        modelImpl.setMinimum(new BigDecimal(1));
        modelImpl.setMaximum(new BigDecimal(100));
        modelImpl.setExclusiveMinimum(true);
        modelImpl.setExclusiveMaximum(true);
        modelImpl.setUniqueItems(true);
        modelImpl.setMinLength(1);
        modelImpl.setMaxLength(10);
        modelImpl.setPattern("Pattern");
        assertEquals(m.writeValueAsString(modelImpl), "{\"minimum\":1,\"maximum\":100,\"multipleOf\":10,\"exclusiveMinimum\":true,\"exclusiveMaximum\":true,\"minLength\":1,\"maxLength\":10,\"pattern\":\"Pattern\",\"uniqueItems\":true}");
    }

    @Test(description = "It should serialize an ArrayModel with new values added in 673")
    public void issue673SerializeArrayModel() throws IOException {
        final ArrayModel arrayModel = new ArrayModel();
        arrayModel.setMultipleOf(new BigDecimal(10));
        arrayModel.setMinimum(new BigDecimal(1));
        arrayModel.setMaximum(new BigDecimal(100));
        arrayModel.setExclusiveMinimum(true);
        arrayModel.setExclusiveMaximum(true);
        arrayModel.setUniqueItems(true);
        arrayModel.setMinLength(1);
        arrayModel.setMaxLength(10);
        arrayModel.setPattern("Pattern");
        assertEquals(m.writeValueAsString(arrayModel), "{\"minimum\":1,\"maximum\":100,\"multipleOf\":10,\"exclusiveMinimum\":true,\"exclusiveMaximum\":true,\"minLength\":1,\"maxLength\":10,\"pattern\":\"Pattern\",\"type\":\"array\",\"uniqueItems\":true}");
    }

    @Test(description = "It should serialize a ComposedModel with new values added in 673")
    public void issue673SerializeComposedModel() throws IOException {
        final ComposedModel composedModel = new ComposedModel();
        composedModel.setMultipleOf(new BigDecimal(10));
        composedModel.setMinimum(new BigDecimal(1));
        composedModel.setMaximum(new BigDecimal(100));
        composedModel.setExclusiveMinimum(true);
        composedModel.setExclusiveMaximum(true);
        composedModel.setMinLength(1);
        composedModel.setMaxLength(10);
        composedModel.setPattern("Pattern");
        assertEquals(m.writeValueAsString(composedModel), "{\"minimum\":1,\"maximum\":100,\"multipleOf\":10,\"exclusiveMinimum\":true,\"exclusiveMaximum\":true,\"minLength\":1,\"maxLength\":10,\"pattern\":\"Pattern\",\"allOf\":[]}");
    }

    @Test(description = "Deserialize New Boolean Values: SwaggerConverter drops some validation properties of body parameters")
    public void testIssue673DeserializeBooleanValues() throws Exception {
        String json = "{\n" +
                "  \"type\": \"boolean\",\n" +
                "  \"exclusiveMaximum\": true,\n" +
                "  \"exclusiveMinimum\": true" +
                "}";

        final ModelImpl modelImpl = Json.mapper().readValue(json, ModelImpl.class);
        final ArrayModel arrayModel = Json.mapper().readValue(json, ArrayModel.class);
        final ComposedModel composedModel = Json.mapper().readValue(json, ComposedModel.class);

        assertEquals(modelImpl.getExclusiveMaximum().booleanValue(), true);
        assertEquals(modelImpl.getExclusiveMinimum().booleanValue(), true);

        assertEquals(arrayModel.getExclusiveMaximum().booleanValue(), true);
        assertEquals(arrayModel.getExclusiveMinimum().booleanValue(), true);

        assertEquals(composedModel.getExclusiveMaximum().booleanValue(), true);
        assertEquals(composedModel.getExclusiveMinimum().booleanValue(), true);
    }

    @Test(description = "Deserialize New Integer Values: SwaggerConverter drops some validation properties of body parameters")
    public void testIssue673DeserializeIntegerValues() throws Exception {
        String json = "{\n" +
                "  \"type\": \"integer\",\n" +
                "  \"minLength\": 1,\n" +
                "  \"maxLength\": 10" +
                "}";

        final ModelImpl modelImpl = Json.mapper().readValue(json, ModelImpl.class);
        final ArrayModel arrayModel = Json.mapper().readValue(json, ArrayModel.class);
        final ComposedModel composedModel = Json.mapper().readValue(json, ComposedModel.class);

        assertEquals(modelImpl.getMinLength().intValue(), 1);
        assertEquals(modelImpl.getMaxLength().intValue(), 10);

        assertEquals(arrayModel.getMinLength().intValue(), 1);
        assertEquals(arrayModel.getMaxLength().intValue(), 10);

        assertEquals(composedModel.getMinLength().intValue(), 1);
        assertEquals(composedModel.getMaxLength().intValue(), 10);
    }

    @Test(description = "Deserialize New Number Values: SwaggerConverter drops some validation properties of body parameters")
    public void testIssue673DeserializeNumberValues() throws Exception {
        String json = "{\n" +
                "  \"type\": \"number\",\n" +
                "  \"multipleOf\": 5" +
                "}";

        final ModelImpl modelImpl = Json.mapper().readValue(json, ModelImpl.class);
        final ArrayModel arrayModel = Json.mapper().readValue(json, ArrayModel.class);
        final ComposedModel composedModel = Json.mapper().readValue(json, ComposedModel.class);

        assertEquals(modelImpl.getMultipleOf().intValue(), 5);
        assertEquals(arrayModel.getMultipleOf().intValue(), 5);
        assertEquals(composedModel.getMultipleOf().intValue(), 5);
    }

    @Test(description = "Deserialize New BigDecimal Values: SwaggerConverter drops some validation properties of body parameters")
    public void testIssue673DeserializeBigDecimalValues() throws Exception {
        String json = "{\n" +
                "  \"type\": \"bigDecimal\",\n" +
                "  \"minimum\": 1,\n" +
                "  \"maximum\": 100" +
                "}";

        final ModelImpl modelImpl = Json.mapper().readValue(json, ModelImpl.class);
        final ArrayModel arrayModel = Json.mapper().readValue(json, ArrayModel.class);
        final ComposedModel composedModel = Json.mapper().readValue(json, ComposedModel.class);

        assertEquals(modelImpl.getMinimum().intValue(), 1);
        assertEquals(modelImpl.getMaximum().intValue(), 100);

        assertEquals(arrayModel.getMinimum().intValue(), 1);
        assertEquals(arrayModel.getMaximum().intValue(), 100);

        assertEquals(composedModel.getMinimum().intValue(), 1);
        assertEquals(composedModel.getMaximum().intValue(), 100);
    }

    @Test(description = "Deserialize New String Values: SwaggerConverter drops some validation properties of body parameters")
    public void testIssue673DeserializeStringValues() throws Exception {
        String json = "{\n" +
                "  \"type\": \"string\",\n" +
                "  \"pattern\": \"Pattern\"" +
                "}";

        final ModelImpl modelImpl = Json.mapper().readValue(json, ModelImpl.class);
        final ArrayModel arrayModel = Json.mapper().readValue(json, ArrayModel.class);
        final ComposedModel composedModel = Json.mapper().readValue(json, ComposedModel.class);

        assertEquals(modelImpl.getPattern(), "Pattern");
        assertEquals(arrayModel.getPattern(), "Pattern");
        assertEquals(composedModel.getPattern(), "Pattern");
    }
}