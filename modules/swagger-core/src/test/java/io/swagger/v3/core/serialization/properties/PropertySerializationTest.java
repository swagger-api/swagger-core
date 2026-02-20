package io.swagger.v3.core.serialization.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.JsonAssert;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MapSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class PropertySerializationTest {
    private final ObjectMapper m = Json.mapper();

    @Test(description = "it should serialize a BooleanSchema")
    public void serializeBooleanSchema() throws IOException {
        final BooleanSchema p = new BooleanSchema()
                ._default(true);
        final String json = "{\"type\":\"boolean\",\"default\":true}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a BooleanSchema")
    public void deserializeBooleanSchema() throws IOException {
        final String json = "{\"type\":\"boolean\",\"default\":false}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "boolean");
        assertNull(p.getFormat());
        assertEquals(p.getClass(), BooleanSchema.class);
        assertEquals(((BooleanSchema) p).getDefault(), Boolean.FALSE);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a DateProperty")
    public void serializeDateProperty() throws IOException {
        final DateSchema p = new DateSchema();
        final String json = "{\"type\":\"string\",\"format\":\"date\"}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a DateProperty")
    public void deserializeDateProperty() throws IOException {
        final String json = "{\"type\":\"string\",\"format\":\"date\"}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "string");
        assertEquals(p.getFormat(), "date");
        assertEquals(p.getClass(), DateSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a DateTimeProperty")
    public void serializeDateTimeProperty() throws IOException {
        final DateTimeSchema p = new DateTimeSchema();
        final String json = "{\"type\":\"string\",\"format\":\"date-time\"}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a DateTimeProperty")
    public void deserializeDateTimeProperty() throws IOException {
        final String json = "{\"type\":\"string\",\"format\":\"date-time\"}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "string");
        assertEquals(p.getFormat(), "date-time");
        assertEquals(p.getClass(), DateTimeSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a DoubleProperty")
    public void serializeDoubleProperty() throws IOException {
        final NumberSchema p = new NumberSchema()
                ._default(new BigDecimal("3.14159"));
        p.format("double");
        final String json = "{\"type\":\"number\",\"format\":\"double\",\"default\":3.14159}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a DoubleProperty")
    public void deserializeDoubleProperty() throws IOException {
        final String json = "{\"type\":\"number\",\"format\":\"double\"}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "number");
        assertEquals(p.getFormat(), "double");
        assertEquals(p.getClass(), NumberSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a FloatProperty")
    public void serializeFloatProperty() throws IOException {
        final NumberSchema p = new NumberSchema()
                ._default(new BigDecimal("1.2"));
        p.format("float");
        final String json = "{\"type\":\"number\",\"format\":\"float\",\"default\":1.2}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a FloatProperty")
    public void deserializeFloatProperty() throws IOException {
        final String json = "{\"type\":\"number\",\"format\":\"float\"}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "number");
        assertEquals(p.getFormat(), "float");
        assertEquals(p.getClass(), NumberSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize an IntegerProperty")
    public void serializeIntegerProperty() throws IOException {
        final IntegerSchema p = new IntegerSchema()
                ._default(32);
        final String json = "{\"type\":\"integer\",\"format\":\"int32\",\"default\":32}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a IntegerProperty")
    public void deserializeIntegerProperty() throws IOException {
        final String json = "{\"type\":\"integer\",\"format\":\"int32\"}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "integer");
        assertEquals(p.getFormat(), "int32");
        assertEquals(p.getClass(), IntegerSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a LongProperty")
    public void serializeLongProperty() throws IOException {
        final IntegerSchema p = new IntegerSchema()
                .format("int64")
                ._default(8675309);
        final String json = "{\"type\":\"integer\",\"format\":\"int64\",\"default\":8675309}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a LongProperty")
    public void deserializeLongProperty() throws IOException {
        final String json = "{\"type\":\"integer\",\"format\":\"int64\"}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "integer");
        assertEquals(p.getFormat(), "int64");
        assertEquals(p.getClass(), IntegerSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a string MapProperty")
    public void serializeStringMapProperty() throws IOException {
        final Schema p = new MapSchema().additionalProperties(new StringSchema());
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a string MapProperty")
    public void deserializeStringMapProperty() throws IOException {
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "object");
        assertEquals(p.getClass(), MapSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a integer MapProperty")
    public void serializeIntegerMapProperty() throws IOException {
        final Schema p = new MapSchema().additionalProperties(new IntegerSchema());
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"integer\",\"format\":\"int32\"}}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a integer MapProperty")
    public void deserializeIntegerMapProperty() throws IOException {
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"integer\",\"format\":\"int32\"}}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "object");
        assertEquals(p.getClass(), MapSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a long MapProperty")
    public void serializeLongMapProperty() throws IOException {
        final Schema p = new MapSchema().additionalProperties(new IntegerSchema().format("int64"));
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"integer\",\"format\":\"int64\"}}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a long MapProperty")
    public void deserializeLongMapProperty() throws IOException {
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"integer\",\"format\":\"int64\"}}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "object");
        assertEquals(p.getClass(), MapSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a RefProperty")
    public void serializeRefProperty() throws IOException {
        final Schema p = new Schema().$ref("#/definitions/Dog");
        final String json = "{\"$ref\":\"#/definitions/Dog\"}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a RefProperty")
    public void deserializeRefProperty() throws IOException {
        final String json = "{\"$ref\":\"#/definitions/Dog\"}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getClass(), Schema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a StringProperty")
    public void serializeStringProperty() throws IOException {
        final StringSchema p = new StringSchema()
                ._default("Bob");
        final String json = "{\"type\":\"string\",\"default\":\"Bob\"}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a StringProperty")
    public void deserializeStringProperty() throws IOException {
        final String json = "{\"type\":\"string\"}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "string");
        assertEquals(p.getClass(), StringSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a StringProperty with enums")
    public void serializeEnumStringProperty() throws IOException {
        final StringSchema p = new StringSchema();
        p._enum(new ArrayList<String>() {{
            this.add("a");
            this.add("b");
        }});
        final String json = "{\"type\":\"string\",\"enum\":[\"a\",\"b\"]}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a StringProperty with enums")
    public void deserializeEnumStringProperty() throws IOException {
        final String json = "{\"type\":\"string\",\"enum\":[\"a\",\"b\"]}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "string");
        List<String> _enum = ((StringSchema) p).getEnum();
        assertNotNull(_enum);
        assertEquals(_enum, Arrays.asList("a", "b"));
        assertEquals(p.getClass(), StringSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize an IntegerProperty with enums")
    public void deserializeEnumIntegerProperty() throws IOException {
        final String json = "{\"type\":\"integer\",\"format\":\"int32\",\"enum\":[1,2]}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "integer");
        List<Number> _enum = ((IntegerSchema) p).getEnum();
        assertNotNull(_enum);
        assertEquals(_enum, Arrays.asList(1, 2));
        assertEquals(p.getClass(), IntegerSchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a string array property")
    public void serializeArrayStringProperty() throws IOException {
        final Schema p = new ArraySchema().items(new StringSchema());
        final String json = "{\"type\":\"array\",\"items\":{\"type\":\"string\"}}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a string array property")
    public void deserializeArrayStringProperty() throws IOException {
        final String json = "{\"type\":\"array\",\"items\":{\"type\":\"string\"}}";
        final Schema p = m.readValue(json, Schema.class);
        assertEquals(p.getType(), "array");
        assertEquals(p.getClass(), ArraySchema.class);
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a string property with readOnly set")
    public void serializeReadOnlyStringProperty() throws IOException {
        final Schema p = new StringSchema().readOnly(true);
        final String json = "{\"type\":\"string\",\"readOnly\":true}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a string property with readOnly unset")
    public void deserializeNotReadOnlyStringProperty() throws IOException {
        final StringSchema p = new StringSchema();
        p.setReadOnly(false);
        final String json = "{\"type\":\"string\",\"readOnly\":false}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize an object property with required set")
    public void serializeObjectPropertyWithRequiredProperties() throws IOException {
        final Schema p = new ObjectSchema()
                .addProperties("stringProperty", new StringSchema());
        p.required(Arrays.asList("stringProperty"));
        final String json = "{\"required\":[\"stringProperty\"],\"type\":\"object\",\"properties\":{\"stringProperty\":{\"type\":\"string\"}}}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize an object property with required set")
    public void deserializeObjectPropertyWithRequiredProperties() throws IOException {
        final Schema p = new ObjectSchema()
                .addProperties("stringProperty", new StringSchema());
        p.required(Arrays.asList("stringProperty"));
        final String json = "{\"type\":\"object\",\"properties\":{\"stringProperty\":{\"type\":\"string\"}},\"required\":[\"stringProperty\"]}";
        JsonAssert.assertJsonEquals(m, m.writeValueAsString(p), json);
    }
}
