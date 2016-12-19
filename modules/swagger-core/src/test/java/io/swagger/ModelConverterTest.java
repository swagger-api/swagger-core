package io.swagger;

import com.google.common.collect.ImmutableSet;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Cat;
import io.swagger.models.ClientOptInput;
import io.swagger.models.Employee;
import io.swagger.models.EmptyModel;
import io.swagger.models.JacksonReadonlyModel;
import io.swagger.models.JodaDateTimeModel;
import io.swagger.models.Model;
import io.swagger.models.Model1155;
import io.swagger.models.ModelImpl;
import io.swagger.models.ModelPropertyName;
import io.swagger.models.ModelWithAltPropertyName;
import io.swagger.models.ModelWithApiModel;
import io.swagger.models.ModelWithEnumArray;
import io.swagger.models.ModelWithFormattedStrings;
import io.swagger.models.ModelWithNumbers;
import io.swagger.models.ModelWithOffset;
import io.swagger.models.ModelWithTuple2;
import io.swagger.models.Person;
import io.swagger.models.composition.AbstractModelWithApiModel;
import io.swagger.models.composition.ModelWithUrlProperty;
import io.swagger.models.composition.Pet;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BaseIntegerProperty;
import io.swagger.models.properties.DecimalProperty;
import io.swagger.models.properties.DoubleProperty;
import io.swagger.models.properties.FloatProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
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

    private Map<String, Model> read(Type type) {
        return ModelConverters.getInstance().read(type);
    }

    private Map<String, Model> readAll(Type type) {
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
        final Map<String, Model> schemas = readAll(ModelWithApiModel.class);
        assertEquals(schemas.size(), 1);
        String model = schemas.keySet().iterator().next();
        assertEquals(model, "MyModel");
    }

    @Test(description = "it should override an inherited model's name")
    public void overrideInheritedModelName() {
        final Map<String, Model> rootSchemas = readAll(AbstractModelWithApiModel.class);
        assertEquals(rootSchemas.size(), 3);
        assertTrue(rootSchemas.containsKey("MyProperty"));
        assertTrue(rootSchemas.containsKey("ModelWithUrlProperty"));
        assertTrue(rootSchemas.containsKey("ModelWithValueProperty"));

        final Map<String, Model> nestedSchemas = readAll(ModelWithUrlProperty.class);
        assertEquals(nestedSchemas.size(), 1);
        assertTrue(nestedSchemas.containsKey("MyProperty"));
    }

    @Test(description = "it should maintain property names")
    public void maintainPropertyNames() {
        final Map<String, Model> schemas = readAll(ModelPropertyName.class);
        assertEquals(schemas.size(), 1);

        final String modelName = schemas.keySet().iterator().next();
        assertEquals(modelName, "ModelPropertyName");

        final Model model = schemas.get(modelName);

        final Iterator<String> itr = new TreeSet(model.getProperties().keySet()).iterator();
        assertEquals(itr.next(), "gettersAndHaters");
        assertEquals(itr.next(), "is_persistent");
    }

    @Test(description = "it should serialize a parameterized type per 606")
    public void serializeParameterizedType() {
        final Map<String, Model> schemas = readAll(Employee.class);

        final ModelImpl employee = (ModelImpl) schemas.get("employee");
        final Map<String, Property> props = employee.getProperties();
        final Iterator<String> et = props.keySet().iterator();

        final Property id = props.get(et.next());
        assertTrue(id instanceof IntegerProperty);

        final Property firstName = props.get(et.next());
        assertTrue(firstName instanceof StringProperty);

        final Property lastName = props.get(et.next());
        assertTrue(lastName instanceof StringProperty);

        final Property department = props.get(et.next());
        assertTrue(department instanceof RefProperty);

        final Property manager = props.get(et.next());
        assertTrue(manager instanceof RefProperty);

        final Property team = props.get(et.next());
        assertTrue(team instanceof ArrayProperty);

        final ArrayProperty ap = (ArrayProperty) team;
        assertTrue(ap.getUniqueItems());

        assertNotNull(employee.getXml());
        assertEquals(employee.getXml().getName(), "employee");
    }

    @Test(description = "it should ignore hidden fields")
    public void ignoreHiddenFields() {
        final Map<String, Model> schemas = readAll(ClientOptInput.class);

        final Model model = schemas.get("ClientOptInput");
        assertEquals(model.getProperties().size(), 2);
    }

    @Test(description = "it should set readOnly per #854")
    public void setReadOnly() {
        final Map<String, Model> schemas = readAll(JacksonReadonlyModel.class);
        final ModelImpl model = (ModelImpl) schemas.get("JacksonReadonlyModel");
        final Property prop = model.getProperties().get("count");
        assertTrue(prop.getReadOnly());
    }

    @Test(description = "it should process a model with org.apache.commons.lang3.tuple.Pair properties")
    public void processModelWithPairProperties() {
        final ModelWithTuple2.TupleAsMapModelConverter asMapConverter = new ModelWithTuple2.TupleAsMapModelConverter(Json.mapper());
        ModelConverters.getInstance().addConverter(asMapConverter);
        final Map<String, Model> asMap = readAll(ModelWithTuple2.class);
        ModelConverters.getInstance().removeConverter(asMapConverter);
        assertEquals(asMap.size(), 4);
        for (String item : Arrays.asList("MapOfString", "MapOfComplexLeft")) {
            ModelImpl model = (ModelImpl) asMap.get(item);
            assertEquals(model.getType(), "object");
            assertNull(model.getProperties());
            assertNotNull(model.getAdditionalProperties());
        }

        final ModelWithTuple2.TupleAsMapPropertyConverter asPropertyConverter = new ModelWithTuple2.TupleAsMapPropertyConverter(Json.mapper());
        ModelConverters.getInstance().addConverter(asPropertyConverter);
        final Map<String, Model> asProperty = readAll(ModelWithTuple2.class);
        ModelConverters.getInstance().removeConverter(asPropertyConverter);
        assertEquals(asProperty.size(), 2);
        for (Map.Entry<String, Property> entry : asProperty.get("ModelWithTuple2").getProperties().entrySet()) {
            String name = entry.getKey();
            Property property = entry.getValue();
            if ("timesheetStates".equals(name)) {
                assertEquals(property.getClass(), MapProperty.class);
            } else if ("manyPairs".equals(name)) {
                assertEquals(property.getClass(), ArrayProperty.class);
                Property items = ((ArrayProperty) property).getItems();
                assertNotNull(items);
                assertEquals(items.getClass(), MapProperty.class);
                Property stringProperty = ((MapProperty) items).getAdditionalProperties();
                assertNotNull(stringProperty);
                assertEquals(stringProperty.getClass(), StringProperty.class);
            } else if ("complexLeft".equals(name)) {
                assertEquals(property.getClass(), ArrayProperty.class);
                Property items = ((ArrayProperty) property).getItems();
                assertNotNull(items);
                assertEquals(items.getClass(), MapProperty.class);
                Property additionalProperty = ((MapProperty) items).getAdditionalProperties();
                assertNotNull(additionalProperty);
                assertEquals(additionalProperty.getClass(), RefProperty.class);
                assertEquals(((RefProperty) additionalProperty).getSimpleRef(), "ComplexLeft");
            } else {
                fail(String.format("Unexpected property: %s", name));
            }
        }
    }

    @Test(description = "it should scan an empty model per 499")
    public void scanEmptyModel() {
        final Map<String, Model> schemas = readAll(EmptyModel.class);
        final ModelImpl model = (ModelImpl) schemas.get("EmptyModel");
        assertNull(model.getProperties());
        assertEquals(model.getType(), "object");
    }

    @Test(description = "it should override the property name")
    public void overridePropertyName() {
        final Map<String, Model> schemas = readAll(ModelWithAltPropertyName.class);
        final Map<String, Property> properties = schemas.get("sample_model").getProperties();
        assertNull(properties.get("id"));
        assertNotNull(properties.get("the_id"));
    }

    @Test(description = "it should convert a model with enum array")
    public void convertModelWithEnumArray() {
        final Map<String, Model> schemas = readAll(ModelWithEnumArray.class);
        assertEquals(schemas.size(), 1);
    }

    private Type getGenericType(Class<?> cls) throws Exception {
        return getClass().getDeclaredMethod("getGenericType", Class.class).getGenericParameterTypes()[0];
    }

    @Test(description = "it should check handling of the Class<?> type")
    public void checkHandlingClassType() throws Exception {
        final Type type = getGenericType(null);
        assertFalse(type instanceof Class<?>);
        final Map<String, Model> schemas = readAll(type);
        assertEquals(schemas.size(), 0);
    }

    @Test(description = "it should convert a model with Formatted strings")
    public void convertModelWithFormattedStrings() throws IOException {
        final Model model = readAll(ModelWithFormattedStrings.class).get("ModelWithFormattedStrings");
        assertEqualsToJson(model, "ModelWithFormattedStrings.json");
    }

    @Test(description = "it should check handling of string types")
    public void checkStringTypesHandling() {
        for (Class<?> cls : Arrays.asList(URI.class, URL.class, UUID.class)) {
            final Map<String, Model> schemas = readAll(cls);
            assertEquals(schemas.size(), 0);
            final Property property = ModelConverters.getInstance().readAsProperty(cls);
            assertNotNull(property);
            assertEquals(property.getType(), "string");
        }
    }

    @Test(description = "it should scan a model per #1155")
    public void scanModel() {
        final Map<String, Model> model = read(Model1155.class);
        assertEquals(model.get("Model1155").getProperties().keySet(), ImmutableSet.of("valid", "value", "is", "get",
                "isA", "getA", "is_persistent", "gettersAndHaters"));
    }

    @Test(description = "it should scan a model with numbers")
    public void scanModelWithNumbers() throws IOException {
        final Map<String, Model> models = readAll(ModelWithNumbers.class);
        assertEquals(models.size(), 1);

        final Model model = models.get("ModelWithNumbers");
        // Check if we get required properties after building models from classes.
        checkModel(model);
        // Check if we get required properties after deserialization from JSON
        checkModel(Json.mapper().readValue(Json.pretty(model), Model.class));
    }

    @Test(description = "it tests a model with java offset")
    public void scanModelWithOffset() throws IOException {
        final Map<String, Model> models = readAll(ModelWithOffset.class);
        assertEquals(models.size(), 1);

        final Model model = models.get("ModelWithOffset");
        Property property = model.getProperties().get("offset");
        assertEquals(property.getType(), "string");
        assertEquals(property.getFormat(), "date-time");
    }

    private void checkType(Property property, Class<?> cls, String type, String format) {
        assertTrue(cls.isInstance(property));
        assertEquals(property.getType(), type);
        assertEquals(property.getFormat(), format);
    }

    private void checkModel(Model model) {
        for (Map.Entry<String, Property> entry : model.getProperties().entrySet()) {
            final String name = entry.getKey();
            final Property property = entry.getValue();
            if (Arrays.asList("shortPrimitive", "shortObject", "intPrimitive", "intObject").contains(name)) {
                checkType(property, IntegerProperty.class, "integer", "int32");
            } else if (Arrays.asList("longPrimitive", "longObject").contains(name)) {
                checkType(property, LongProperty.class, "integer", "int64");
            } else if (Arrays.asList("floatPrimitive", "floatObject").contains(name)) {
                checkType(property, FloatProperty.class, "number", "float");
            } else if (Arrays.asList("doublePrimitive", "doubleObject").contains(name)) {
                checkType(property, DoubleProperty.class, "number", "double");
            } else if ("bigInteger".equals(name)) {
                checkType(property, BaseIntegerProperty.class, "integer", null);
            } else if ("bigDecimal".equals(name)) {
                checkType(property, DecimalProperty.class, "number", null);
            } else {
                fail(String.format("Unexpected property: %s", name));
            }
        }
    }

    @Test
    public void formatDate() {
        final Map<String, Model> models = ModelConverters.getInstance().read(DateModel.class);
        final Model model = models.get("DateModel");
        assertEquals(model.getProperties().size(), 5);
        final String json = "{" +
                "   \"type\":\"object\"," +
                "   \"properties\":{" +
                "      \"date\":{" +
                "         \"type\":\"string\"," +
                "         \"format\":\"date-time\"," +
                "         \"position\":1" +
                "      }," +
                "      \"intValue\":{" +
                "         \"type\":\"integer\"," +
                "         \"format\":\"int32\"," +
                "         \"position\":2" +
                "      }," +
                "      \"longValue\":{" +
                "         \"type\":\"integer\"," +
                "         \"format\":\"int64\"," +
                "         \"position\":3" +
                "      }," +
                "      \"floatValue\":{" +
                "         \"type\":\"number\"," +
                "         \"format\":\"float\"," +
                "         \"position\":4" +
                "      }," +
                "      \"doubleValue\":{" +
                "         \"type\":\"number\"," +
                "         \"format\":\"double\"," +
                "         \"position\":5" +
                "      }" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(model, json);
    }

    class DateModel {
        @ApiModelProperty(position = 1)
        public Date date;
        @ApiModelProperty(position=2)
        public int intValue;
        @ApiModelProperty(position=3)
        public Long longValue;
        @ApiModelProperty(position=4)
        public Float floatValue;
        @ApiModelProperty(position=5)
        public Double doubleValue;
    }
}
