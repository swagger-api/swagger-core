package io.swagger.v3.core.serialization;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.oas.models.*;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.media.*;
import org.testng.annotations.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class ModelSerializerTest {
    private final ObjectMapper m = Json.mapper();

    @Test(description = "it should convert a model")
    public void convertModel() throws JacksonException {
        final Schema pet = new Schema();
        final Map<String, Schema> props = new LinkedHashMap<>();
        props.put("intValue", new IntegerSchema());
        props.put("longValue", new IntegerSchema().format("int64"));
        props.put("dateValue", new DateSchema());
        props.put("dateTimeValue", new DateTimeSchema());
        pet.setProperties(props);
        pet.setRequired(Arrays.asList("intValue", "name"));
        final String json = """
                {
                   "required":[
                      "intValue"
                   ],
                   "properties":{
                      "intValue":{
                         "type":"integer",
                         "format":"int32"
                      },
                      "longValue":{
                         "type":"integer",
                         "format":"int64"
                      },
                      "dateValue":{
                         "type":"string",
                         "format":"date"
                      },
                      "dateTimeValue":{
                         "type":"string",
                         "format":"date-time"
                      }
                   }
                }
                """;

        SerializationMatchers.assertEqualsToJson(pet, json);
    }

    @Test(description = "it should deserialize a model")
    public void deserializeModel() {
        final String json = """
                {
                   "required":[
                      "intValue"
                   ],
                   "type":"object",
                   "properties":{
                      "dateValue":{
                         "type":"string",
                         "format":"date"
                      },
                      "longValue":{
                         "type":"integer",
                         "format":"int64"
                      },
                      "dateTimeValue":{
                         "type":"string",
                         "format":"date-time"
                      },
                      "intValue":{
                         "type":"integer",
                         "format":"int32"
                      },
                      "byteArrayValue":{
                         "type":"string",
                         "format":"binary"
                      }
                   }
                }
                """;

        final Schema p = m.readValue(json, Schema.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should serialize an array model")
    public void serializeArrayModel() {
        final ArraySchema model = new ArraySchema();
        model.setItems(new Schema().$ref("Pet"));
        assertEquals(m.writeValueAsString(model), "{\"type\":\"array\",\"items\":{\"$ref\":\"#/components/schemas/Pet\"}}");
    }

    @Test(description = "it should deserialize an array model")
    public void deserializeArrayModel() {
        final String json = "{\"type\":\"array\",\"items\":{\"$ref\":\"#/definitions/Pet\"}}";
        final Schema p = m.readValue(json, Schema.class);
        assertTrue(p instanceof ArraySchema);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should not create an xml object for $ref")
    public void shouldNotCreateXmlObjectForRef() {
        final Schema model = new Schema().$ref("Monster");
        model.setDescription("oops");
        model.setExternalDocs(new ExternalDocumentation()
                .description("external docs")
                .url("http://swagger.io"));
        assertEquals(Json.mapper().writeValueAsString(model), "{\"$ref\":\"#/components/schemas/Monster\"}");
    }

    @Test(description = "it should make a field readOnly by annotation")
    public void makeFieldReadOnly() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().read(Car.class);
        final String json = """
                {
                   "Car":{
                      "type":"object",
                      "properties":{
                         "wheelCount":{
                            "type":"integer",
                            "format":"int32",
                            "readOnly":true
                         }
                      }
                   }
                }
                """;
        SerializationMatchers.assertEqualsToJson(schemas, json);
    }

    @Test(description = "it should serialize a model with a Set")
    public void serializeModelWithSet() {
        final Map<String, Schema> schemas = ModelConverters.getInstance().read(Manufacturers.class);
        final String json = """
                {
                   "Manufacturers":{
                      "type":"object",
                      "properties":{
                         "countries":{
                            "type":"array",
                            "uniqueItems":true,
                            "items":{
                               "type":"string"
                            }
                         }
                      }
                   }
                }
                """;
        SerializationMatchers.assertEqualsToJson(schemas, json);
    }

    @Test(description = "it should deserialize a model with object example")
    public void deserializeModelWithObjectExample() {
        final String json = """
                {
                   "title":"Error",
                   "type":"object",
                   "properties":{
                      "code":{
                         "type":"integer",
                         "format":"int32"
                      },
                      "message":{
                         "type":"string"
                      },
                      "fields":{
                         "type":"string"
                      }
                   },
                   "example":{
                      "code":1,
                      "message":"hello",
                      "fields":"abc"
                   }
                }
                """;

        final Schema model = Json.mapper().readValue(json, Schema.class);
        assertEquals(Json.mapper().writeValueAsString(model.getExample()), "{\"code\":1,\"message\":\"hello\",\"fields\":\"abc\"}");
    }

    @Test(description = "it should deserialize a model with read-only property")
    public void deserializeModelWithReadOnlyProperty() {
        final String json = """
                {
                   "properties":{
                      "id":{
                         "type":"integer",
                         "format":"int32",
                         "readOnly":true
                      }
                   }
                }
                """;
        final Schema model = Json.mapper().readValue(json, Schema.class);
        Schema property = (Schema) model.getProperties().get("id");
        assertTrue(property.getReadOnly());
    }

    @Test(description = "it should generate a JSON with read-only from pojo, #1161")
    public void readOnlyJsonGeneration() {
        Map<String, Schema> models = ModelConverters.getInstance().read(ReadOnlyModel.class);

        Schema model = models.get("ReadOnlyModel");

        Schema id = (Schema) model.getProperties().get("id");
        assertTrue(id.getReadOnly());

        Schema readWriteId = (Schema) model.getProperties().get("readWriteId");
        assertNull(readWriteId.getReadOnly());
    }

    @Test(description = "it should generate an integer field with enum")
    public void integerEnumGeneration() {
        final String json = """
                {
                   "properties":{
                      "id":{
                         "description":"fun!",
                         "type":"integer",
                         "format":"int32",
                         "readOnly":true,
                         "enum": [ 0, 1]
                      }
                   }
                }
                """;
        final Schema model = Json.mapper().readValue(json, Schema.class);
        IntegerSchema p = (IntegerSchema) model.getProperties().get("id");

        assertNotNull(p.getEnum());
        assertEquals(p.getEnum().get(0), new Integer(0));
        assertEquals(p.getEnum().get(1), new Integer(1));
    }

    @Test(description = "it retains enums per ")
    public void testEnumParser() {
        String json = """
                {
                  "properties": {
                    "AdvStateType": {
                      "description": "Advertising State",
                      "enum": [
                        "off",
                        "on"
                      ],
                      "type": "string"
                    }
                  }
                }
                """;
        final Schema model = Json.mapper().readValue(json, Schema.class);
        StringSchema p = (StringSchema) model.getProperties().get("AdvStateType");

        assertNotNull(p.getEnum());
        assertEquals(p.getEnum().get(0), "off");
        assertEquals(p.getEnum().get(1), "on");
    }

    @Test
    public void testPrimitiveModel() {
        String json = """
                {
                  "type": "string",
                  "enum": [
                    "a",
                    "b",
                    "c"
                  ]
                }
                """;

        final Schema model = Json.mapper().readValue(json, Schema.class);

        assertNotNull(model.getEnum());
        assertTrue(model.getEnum().size() == 3);
    }

    @Test
    public void testIssue1852() {
        String json = """
                {
                  "type": "integer",
                  "minimum": 10,
                  "maximum": 20,
                  "default": 15
                }
                """;

        final Schema model = Json.mapper().readValue(json, Schema.class);

        assertEquals(model.getMinimum().intValue(), 10);
        assertEquals(model.getMaximum().intValue(), 20);
        assertEquals(model.getDefault(), 15);
    }

    @Test
    public void testIssue2064Neg() {
        String json = """
                {
                  "type": "string",
                  "uniqueItems": false
                }
                """;

        final Schema model = Json.mapper().readValue(json, Schema.class);

        assertFalse(model.getUniqueItems());
    }

    @Test
    public void testIssue2064() {
        String json = """
                {
                  "type": "string",
                  "uniqueItems": true
                }
                """;

        final Schema model = Json.mapper().readValue(json, Schema.class);

        assertTrue(model.getUniqueItems());
    }

    @Test
    public void testIssue2064Ip() {
        String json = """
                {
                  "type": "object",
                  "properties": {
                    "id": {
                      "type": "integer",
                      "format": "int32",
                      "multipleOf": 3.0
                    }
                  }
                }
                """;

        final Schema model = Json.mapper().readValue(json, Schema.class);

        IntegerSchema ip = (IntegerSchema) model.getProperties().get("id");
        assertEquals(ip.getMultipleOf(), new BigDecimal("3.0"));

    }

    @Test
    public void testEnumWithNull() {
        String yaml = """
                type: integer
                description: some int values with null
                format: int32
                enum:
                - 1
                - 2
                - null
                """;

        final Schema model = Yaml.mapper().readValue(yaml, Schema.class);

        assertEquals(model.getEnum(), Arrays.asList(1, 2, null));
        SerializationMatchers.assertEqualsToYaml(model, yaml);

    }
}
