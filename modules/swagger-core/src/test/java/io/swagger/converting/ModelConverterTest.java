/**
 * Copyright 2017 SmartBear Software
 * (c) Copyright 2017 Hewlett Packard Enterprise Development LP
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.swagger.converting;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.oas.models.Cat;
import io.swagger.oas.models.ClientOptInput;
import io.swagger.oas.models.Employee;
import io.swagger.oas.models.EmptyModel;
import io.swagger.oas.models.JacksonReadonlyModel;
import io.swagger.oas.models.JodaDateTimeModel;
import io.swagger.oas.models.JsonValueModel;
import io.swagger.oas.models.Model1155;
import io.swagger.oas.models.ModelPropertyName;
import io.swagger.oas.models.ModelWithAltPropertyName;
import io.swagger.oas.models.ModelWithApiModel;
import io.swagger.oas.models.ModelWithEnumArray;
import io.swagger.oas.models.ModelWithFormattedStrings;
import io.swagger.oas.models.ModelWithNumbers;
import io.swagger.oas.models.ModelWithOffset;
import io.swagger.oas.models.ModelWithTuple2;
import io.swagger.oas.models.Person;
import io.swagger.oas.models.composition.AbstractModelWithApiModel;
import io.swagger.oas.models.composition.ModelWithUrlProperty;
import io.swagger.oas.models.composition.Pet;
import io.swagger.oas.models.media.ArraySchema;
import io.swagger.oas.models.media.IntegerSchema;
import io.swagger.oas.models.media.MapSchema;
import io.swagger.oas.models.media.NumberSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import io.swagger.util.Json;
import io.swagger.util.ResourceUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class ModelConverterTest {

    private Map<String, Schema> read(Type type) {
        return ModelConverters.getInstance().read(type);
    }

    private Map<String, Schema> readAll(Type type) {
        return ModelConverters.getInstance().readAll(type);
    }

    private void assertEqualsToJson( Object objectToSerialize, String fileName)  throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), fileName);
        SerializationMatchers.assertEqualsToJson(objectToSerialize, json);
    }

    @Test(description = "it should convert a model")
    public void convertModel() throws IOException {
        assertEqualsToJson(read(Person.class), "Person.json");
    }

    @Test(description = "it should convert a model with Joda DateTime")
    public void convertModelWithJodaDateTime() throws IOException {
        assertEqualsToJson(read(JodaDateTimeModel.class), "JodaDateTimeModel.json");
    }

    @Test(description = "read an interface")
    public void readInterface() throws IOException {
        assertEqualsToJson(readAll(Pet.class), "Pet.json");
    }

    @Test(description = "it should read an inherited interface")
    public void readInheritedInterface() throws IOException {
        assertEqualsToJson(readAll(Cat.class), "Cat.json");
    }

    @Test(description = "it should honor the ApiModel name")
    public void honorApiModelName() {
        final Map<String, Schema> schemas = readAll(ModelWithApiModel.class);
        assertEquals(schemas.size(), 1);
        String model = schemas.keySet().iterator().next();
        assertEquals(model, "MyModel");
    }

    @Test(description = "it should override an inherited model's name")
    public void overrideInheritedModelName() {
        final Map<String, Schema> rootSchemas = readAll(AbstractModelWithApiModel.class);
        assertEquals(rootSchemas.size(), 3);
        assertTrue(rootSchemas.containsKey("MyProperty"));
        assertTrue(rootSchemas.containsKey("ModelWithUrlProperty"));
        assertTrue(rootSchemas.containsKey("ModelWithValueProperty"));

        final Map<String, Schema> nestedSchemas = readAll(ModelWithUrlProperty.class);
        assertEquals(nestedSchemas.size(), 1);
        assertTrue(nestedSchemas.containsKey("MyProperty"));
    }

    @Test(description = "it should maintain property names")
    public void maintainPropertyNames() {
        final Map<String, Schema> schemas = readAll(ModelPropertyName.class);
        assertEquals(schemas.size(), 1);

        final String modelName = schemas.keySet().iterator().next();
        assertEquals(modelName, "ModelPropertyName");

        final Schema model = schemas.get(modelName);

        final Iterator<String> itr = new TreeSet(model.getProperties().keySet()).iterator();
        assertEquals(itr.next(), "gettersAndHaters");
        assertEquals(itr.next(), "is_persistent");
    }

    @Test(description = "it should serialize a parameterized type per 606")
    public void serializeParameterizedType() {
        final Map<String, Schema> schemas = readAll(Employee.class);

        final Schema employee = (Schema) schemas.get("employee");
        final Map<String, Schema> props = employee.getProperties();
        final Iterator<String> et = props.keySet().iterator();

        final Schema id = props.get(et.next());
        assertTrue(id instanceof IntegerSchema);

        final Schema firstName = props.get(et.next());
        assertTrue(firstName instanceof StringSchema);

        final Schema lastName = props.get(et.next());
        assertTrue(lastName instanceof StringSchema);

        final Schema department = props.get(et.next());
        assertNotNull(department.get$ref());

        final Schema manager = props.get(et.next());
        assertNotNull(manager.get$ref());

        final Schema team = props.get(et.next());
        assertTrue(team instanceof ArraySchema);

        final ArraySchema ap = (ArraySchema) team;
        assertTrue(ap.getUniqueItems());

        assertNotNull(employee.getXml());
        assertEquals(employee.getXml().getName(), "employee");
    }

    @Test(description = "it should ignore hidden fields")
    public void ignoreHiddenFields() {
        final Map<String, Schema> schemas = readAll(ClientOptInput.class);

        final Schema model = schemas.get("ClientOptInput");
        assertEquals(model.getProperties().size(), 2);
    }

    @Test(description = "it should set readOnly per #854")
    public void setReadOnly() {
        final Map<String, Schema> schemas = readAll(JacksonReadonlyModel.class);
        final Schema model = (Schema) schemas.get("JacksonReadonlyModel");
        final Schema prop = (Schema)model.getProperties().get("count");
        assertTrue(prop.getReadOnly());
    }

    @Test(description = "it should process a model with org.apache.commons.lang3.tuple.Pair properties")
    public void processModelWithPairProperties() {
        final ModelWithTuple2.TupleAsMapModelConverter asMapConverter = new ModelWithTuple2.TupleAsMapModelConverter(Json.mapper());
        ModelConverters.getInstance().addConverter(asMapConverter);
        final Map<String, Schema> asMap = readAll(ModelWithTuple2.class);
        ModelConverters.getInstance().removeConverter(asMapConverter);
        assertEquals(asMap.size(), 4);
        for (String item : Arrays.asList("MapOfString", "MapOfComplexLeft")) {
            Schema model = (Schema) asMap.get(item);
            assertEquals(model.getType(), "object");
            assertNull(model.getProperties());
            assertNotNull(model.getAdditionalProperties());
        }

        final ModelWithTuple2.TupleAsMapPropertyConverter asPropertyConverter = new ModelWithTuple2.TupleAsMapPropertyConverter(Json.mapper());
        ModelConverters.getInstance().addConverter(asPropertyConverter);
        final Map<String, Schema> asProperty = readAll(ModelWithTuple2.class);
        ModelConverters.getInstance().removeConverter(asPropertyConverter);
        assertEquals(asProperty.size(), 2);
        Map<String, Schema> values = asProperty.get("ModelWithTuple2").getProperties();
        for (Map.Entry<String, Schema> entry : values.entrySet()) {
            String name = entry.getKey();
            Schema property = entry.getValue();
            if ("timesheetStates".equals(name)) {
                assertEquals(property.getClass(), MapSchema.class);
            } else if ("manyPairs".equals(name)) {
                assertEquals(property.getClass(), ArraySchema.class);
                Schema items = ((ArraySchema) property).getItems();
                assertNotNull(items);
                assertEquals(items.getClass(), MapSchema.class);
                Schema stringProperty = ((MapSchema) items).getAdditionalProperties();
                assertNotNull(stringProperty);
                assertEquals(stringProperty.getClass(), StringSchema.class);
            } else if ("complexLeft".equals(name)) {
                assertEquals(property.getClass(), ArraySchema.class);
                Schema items = ((ArraySchema) property).getItems();
                assertNotNull(items);
                assertEquals(items.getClass(), MapSchema.class);
                Schema additionalProperty = ((MapSchema) items).getAdditionalProperties();
                assertNotNull(additionalProperty);
                assertNotNull(additionalProperty.get$ref());
                assertEquals(additionalProperty.get$ref(), "#/components/schemas/ComplexLeft");
            } else {
                fail(String.format("Unexpected property: %s", name));
            }
        }
    }

    @Test(description = "it should scan an empty model per 499")
    public void scanEmptyModel() {
        final Map<String, Schema> schemas = readAll(EmptyModel.class);
        final Schema model = (Schema) schemas.get("EmptyModel");
        assertNull(model.getProperties());
        assertEquals(model.getType(), "object");
    }

    @Test(description = "it should override the property name")
    public void overridePropertyName() {
        final Map<String, Schema> schemas = readAll(ModelWithAltPropertyName.class);
        final Map<String, Schema> properties = schemas.get("sample_model").getProperties();
        assertNull(properties.get("id"));
        assertNotNull(properties.get("the_id"));
    }

    @Test(description = "it should convert a model with enum array")
    public void convertModelWithEnumArray() {
        final Map<String, Schema> schemas = readAll(ModelWithEnumArray.class);
        assertEquals(schemas.size(), 1);
    }

    private Type getGenericType(Class<?> cls) throws Exception {
        return getClass().getDeclaredMethod("getGenericType", Class.class).getGenericParameterTypes()[0];
    }

    @Test(description = "it should check handling of the Class<?> type")
    public void checkHandlingClassType() throws Exception {
        final Type type = getGenericType(null);
        assertFalse(type instanceof Class<?>);
        final Map<String, Schema> schemas = readAll(type);
        assertEquals(schemas.size(), 0);
    }

    @Test(description = "it should convert a model with Formatted strings")
    public void convertModelWithFormattedStrings() throws IOException {
        final Schema model = readAll(ModelWithFormattedStrings.class).get("ModelWithFormattedStrings");
        assertEqualsToJson(model, "ModelWithFormattedStrings.json");
    }

    @Test(description = "it should check handling of string types")
    public void checkStringTypesHandling() {
        for (Class<?> cls : Arrays.asList(URI.class, URL.class, UUID.class)) {
            final Map<String, Schema> schemas = readAll(cls);
            assertEquals(schemas.size(), 0);
            final Schema property = ModelConverters.getInstance().resolveProperty(cls);
            assertNotNull(property);
            assertEquals(property.getType(), "string");
        }
    }

    @Test(description = "it should scan a model per #1155")
    public void scanModel() {
        final Map<String, Schema> model = read(Model1155.class);
        assertEquals(model.get("Model1155").getProperties().keySet(), ImmutableSet.of("valid", "value", "is", "get",
                "isA", "getA", "is_persistent", "gettersAndHaters"));
    }

    @Test(description = "it should scan a model with numbers")
    public void scanModelWithNumbers() throws IOException {
        final Map<String, Schema> models = readAll(ModelWithNumbers.class);
        assertEquals(models.size(), 1);

        final Schema model = models.get("ModelWithNumbers");
        // Check if we get required properties after building models from classes.
        checkModel(model);
        // Check if we get required properties after deserialization from JSON
        checkModel(Json.mapper().readValue(Json.pretty(model), Schema.class));
    }

    @Test(description = "it tests a model with java offset")
    public void scanModelWithOffset() throws IOException {
        final Map<String, Schema> models = readAll(ModelWithOffset.class);
        assertEquals(models.size(), 1);

        final Schema model = models.get("ModelWithOffset");
        Schema property = (Schema)model.getProperties().get("offset");
        assertEquals(property.getType(), "string");
        assertEquals(property.getFormat(), "date-time");
    }
    
    @Test(description = "it tests a model using a JsonValue method to override its type")
    public void checkJsonValueHandling() {
        final Map<String, Schema> schemas = readAll(JsonValueModel.class);
        final Map<String, Schema> properties = schemas.get("JsonValueModel").getProperties();
        // "value" is a class with a JsonValue method returning String, so should be string type.
        assertNotNull(properties.get("value"));
        assertEquals(properties.get("value").getType(), "string");
        // "inner" is a class (InnerBean) with a JsonValue method returning ComplexBean
        assertNotNull(properties.get("inner"));
        assertEquals(properties.get("inner").get$ref(), "#/components/schemas/ComplexBean");
        // Check ComplexBean has the correct fields and has not been mapped to InnerBean.
        final Map<String, Schema> innerProperties = schemas.get("ComplexBean").getProperties();
        assertEquals(innerProperties.keySet(), Sets.newHashSet("count", "name"));
        // "list" is a class (ListBean) with a JsonValue method returning a list
        assertNotNull(properties.get("list"));
        assertEquals(properties.get("list").getType(), "array");
        // ListBean should not be a resolved type, as it should just map to list
        assertNull(schemas.get("ListBean"));
    }

    private void checkType(Schema property, Class<?> cls, String type, String format) {
        assertTrue(cls.isInstance(property));
        assertEquals(property.getType(), type);
        if(format == null) {
            assertNull(property.getFormat());
        }
        else {
            assertEquals(property.getFormat(), format);
        }
    }

    private void checkModel(Schema model) {
        Map<String, Schema> props = model.getProperties();
        for (Map.Entry<String, Schema> entry : props.entrySet()) {
            final String name = entry.getKey();
            final Schema property = entry.getValue();
            if (Arrays.asList("shortPrimitive", "shortObject", "intPrimitive", "intObject").contains(name)) {
                checkType(property, IntegerSchema.class, "integer", "int32");
            } else if (Arrays.asList("longPrimitive", "longObject").contains(name)) {
                checkType(property, IntegerSchema.class, "integer", "int64");
            } else if (Arrays.asList("floatPrimitive", "floatObject").contains(name)) {
                checkType(property, NumberSchema.class, "number", "float");
            } else if (Arrays.asList("doublePrimitive", "doubleObject").contains(name)) {
                checkType(property, NumberSchema.class, "number", "double");
            } else if ("bigInteger".equals(name)) {
                checkType(property, IntegerSchema.class, "integer", null);
            } else if ("bigDecimal".equals(name)) {
                checkType(property, NumberSchema.class, "number", null);
            } else {
                fail(String.format("Unexpected property: %s", name));
            }
        }
    }

    @Test
    public void formatDate() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(DateModel.class);
        final Schema model = models.get("DateModel");
        assertEquals(model.getProperties().size(), 5);
        final String json =
                "{" +
                "   \"type\":\"object\"," +
                "   \"properties\":{" +
                "      \"date\":{" +
                "         \"type\":\"string\"," +
                "         \"format\":\"date-time\"" +
                "      }," +
                "      \"intValue\":{" +
                "         \"type\":\"integer\"," +
                "         \"format\":\"int32\"" +
                "      }," +
                "      \"longValue\":{" +
                "         \"type\":\"integer\"," +
                "         \"format\":\"int64\"" +
                "      }," +
                "      \"floatValue\":{" +
                "         \"type\":\"number\"," +
                "         \"format\":\"float\"" +
                "      }," +
                "      \"doubleValue\":{" +
                "         \"type\":\"number\"," +
                "         \"format\":\"double\"" +
                "      }" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(model, json);
    }

    class DateModel {
        @io.swagger.oas.annotations.media.Schema
        public Date date;
        @io.swagger.oas.annotations.media.Schema
        public int intValue;
        @io.swagger.oas.annotations.media.Schema
        public Long longValue;
        @io.swagger.oas.annotations.media.Schema
        public Float floatValue;
        @io.swagger.oas.annotations.media.Schema
        public Double doubleValue;
    }
}
