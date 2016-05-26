package io.swagger.properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.FloatProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.ObjectProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import io.swagger.util.Json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PropertySerializationTest {
    private final ObjectMapper m = Json.mapper();

    @Test(description = "it should serialize a BooleanProperty")
    public void serializeBooleanProperty() throws IOException {
        final BooleanProperty p = new BooleanProperty()
                ._default(true);
        final String json = "{\"type\":\"boolean\",\"default\":true}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a BooleanProperty")
    public void deserializeBooleanProperty() throws IOException {
        final String json = "{\"type\":\"boolean\",\"default\":false}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "boolean");
        assertNull(p.getFormat());
        assertEquals(p.getClass(), BooleanProperty.class);
        assertEquals(((BooleanProperty) p).getDefault(), Boolean.FALSE);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a DateProperty")
    public void serializeDateProperty() throws IOException {
        final DateProperty p = new DateProperty();
        final String json = "{\"type\":\"string\",\"format\":\"date\"}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a DateProperty")
    public void deserializeDateProperty() throws IOException {
        final String json = "{\"type\":\"string\",\"format\":\"date\"}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "string");
        assertEquals(p.getFormat(), "date");
        assertEquals(p.getClass(), DateProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a DateTimeProperty")
    public void serializeDateTimeProperty() throws IOException {
        final DateTimeProperty p = new DateTimeProperty();
        final String json = "{\"type\":\"string\",\"format\":\"date-time\"}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a DateTimeProperty")
    public void deserializeDateTimeProperty() throws IOException {
        final String json = "{\"type\":\"string\",\"format\":\"date-time\"}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "string");
        assertEquals(p.getFormat(), "date-time");
        assertEquals(p.getClass(), DateTimeProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a DoubleProperty")
    public void serializeDoubleProperty() throws IOException {
        final DoubleProperty p = new DoubleProperty()
                ._default(3.14159);
        final String json = "{\"type\":\"number\",\"format\":\"double\",\"default\":3.14159}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a DoubleProperty")
    public void deserializeDoubleProperty() throws IOException {
        final String json = "{\"type\":\"number\",\"format\":\"double\"}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "number");
        assertEquals(p.getFormat(), "double");
        assertEquals(p.getClass(), DoubleProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a FloatProperty")
    public void serializeFloatProperty() throws IOException {
        final FloatProperty p = new FloatProperty()
                ._default(1.20f);
        final String json = "{\"type\":\"number\",\"format\":\"float\",\"default\":1.2}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a FloatProperty")
    public void deserializeFloatProperty() throws IOException {
        final String json = "{\"type\":\"number\",\"format\":\"float\"}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "number");
        assertEquals(p.getFormat(), "float");
        assertEquals(p.getClass(), FloatProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize an IntegerProperty")
    public void serializeIntegerProperty() throws IOException {
        final IntegerProperty p = new IntegerProperty()
                ._default(32);
        final String json = "{\"type\":\"integer\",\"format\":\"int32\",\"default\":32}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a IntegerProperty")
    public void deserializeIntegerProperty() throws IOException {
        final String json = "{\"type\":\"integer\",\"format\":\"int32\"}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "integer");
        assertEquals(p.getFormat(), "int32");
        assertEquals(p.getClass(), IntegerProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a LongProperty")
    public void serializeLongProperty() throws IOException {
        final LongProperty p = new LongProperty()
                ._default(8675309L);
        final String json = "{\"type\":\"integer\",\"format\":\"int64\",\"default\":8675309}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a LongProperty")
    public void deserializeLongProperty() throws IOException {
        final String json = "{\"type\":\"integer\",\"format\":\"int64\"}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "integer");
        assertEquals(p.getFormat(), "int64");
        assertEquals(p.getClass(), LongProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a string MapProperty")
    public void serializeStringMapProperty() throws IOException {
        final MapProperty p = new MapProperty(new StringProperty());
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a string MapProperty")
    public void deserializeStringMapProperty() throws IOException {
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"string\"}}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "object");
        assertEquals(p.getClass(), MapProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a integer MapProperty")
    public void serializeIntegerMapProperty() throws IOException {
        final MapProperty p = new MapProperty(new IntegerProperty());
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"integer\",\"format\":\"int32\"}}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a integer MapProperty")
    public void deserializeIntegerMapProperty() throws IOException {
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"integer\",\"format\":\"int32\"}}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "object");
        assertEquals(p.getClass(), MapProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a long MapProperty")
    public void serializeLongMapProperty() throws IOException {
        final MapProperty p = new MapProperty(new LongProperty());
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"integer\",\"format\":\"int64\"}}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a long MapProperty")
    public void deserializeLongMapProperty() throws IOException {
        final String json = "{\"type\":\"object\",\"additionalProperties\":{\"type\":\"integer\",\"format\":\"int64\"}}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "object");
        assertEquals(p.getClass(), MapProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a RefProperty")
    public void serializeRefProperty() throws IOException {
        final RefProperty p = new RefProperty("Dog");
        final String json = "{\"$ref\":\"#/definitions/Dog\"}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a RefProperty")
    public void deserializeRefProperty() throws IOException {
        final String json = "{\"$ref\":\"#/definitions/Dog\"}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getClass(), RefProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a StringProperty")
    public void serializeStringProperty() throws IOException {
        final StringProperty p = new StringProperty()
                ._default("Bob");
        final String json = "{\"type\":\"string\",\"default\":\"Bob\"}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a StringProperty")
    public void deserializeStringProperty() throws IOException {
        final String json = "{\"type\":\"string\"}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "string");
        assertEquals(p.getClass(), StringProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a StringProperty with enums")
    public void serializeEnumStringProperty() throws IOException {
        final StringProperty p = new StringProperty()._enum("a")._enum("b");
        final String json = "{\"type\":\"string\",\"enum\":[\"a\",\"b\"]}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a StringProperty with enums")
    public void deserializeEnumStringProperty() throws IOException {
        final String json = "{\"type\":\"string\",\"enum\":[\"a\",\"b\"]}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "string");
        List<String> _enum = ((StringProperty) p).getEnum();
        assertNotNull(_enum);
        assertEquals(_enum, Arrays.asList("a", "b"));
        assertEquals(p.getClass(), StringProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize an IntegerProperty with enums")
    public void deserializeEnumIntegerProperty() throws IOException {
        final String json = "{\"type\":\"integer\",\"format\":\"int32\",\"enum\":[1,2]}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "integer");
        List<Integer> _enum = ((IntegerProperty) p).getEnum();
        assertNotNull(_enum);
        assertEquals(_enum, Arrays.asList(1, 2));
        assertEquals(p.getClass(), IntegerProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a string array property")
    public void serializeArrayStringProperty() throws IOException {
        final ArrayProperty p = new ArrayProperty().items(new StringProperty());
        final String json = "{\"type\":\"array\",\"items\":{\"type\":\"string\"}}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize a string array property")
    public void deserializeArrayStringProperty() throws IOException {
        final String json = "{\"type\":\"array\",\"items\":{\"type\":\"string\"}}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "array");
        assertEquals(p.getClass(), ArrayProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a string property with readOnly set")
    public void serializeReadOnlyStringProperty() throws IOException {
        final Property p = new StringProperty().readOnly();
        final String json = "{\"type\":\"string\",\"readOnly\":true}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize a string property with readOnly unset")
    public void deserializeNotReadOnlyStringProperty() throws IOException {
        final StringProperty p = new StringProperty();
        p.setReadOnly(false);
        final String json = "{\"type\":\"string\"}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should read a file property")
    public void serializeFileProperty() throws IOException {
        final String json = "{\"type\":\"file\"}";
        final Property p = m.readValue(json, Property.class);
        assertEquals(p.getType(), "file");
        assertEquals(p.getClass(), FileProperty.class);
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should serialize an object property with required set")
    public void serializeObjectPropertyWithRequiredProperties() throws IOException {
        final ObjectProperty p = new ObjectProperty()
                .property("stringProperty", new StringProperty()
                        .required(true));
        final String json = "{\"type\":\"object\",\"properties\":{\"stringProperty\":{\"type\":\"string\"}},\"required\":[\"stringProperty\"]}";
        assertEquals(m.writeValueAsString(p), json);
    }

    @Test(description = "it should deserialize an object property with required set")
    public void deserializeObjectPropertyWithRequiredProperties() throws IOException {
        final ObjectProperty p = new ObjectProperty()
                .property("stringProperty", new StringProperty()
                        .required(true));
        final String json = "{\"type\":\"object\",\"properties\":{\"stringProperty\":{\"type\":\"string\"}},\"required\":[\"stringProperty\"]}";
        assertEquals(p, m.readValue(json, ObjectProperty.class));
    }
}
