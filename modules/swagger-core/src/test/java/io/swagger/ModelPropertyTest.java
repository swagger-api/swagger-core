package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Model;
import io.swagger.models.Model1979;
import io.swagger.models.ModelImpl;
import io.swagger.models.ModelWithBooleanProperty;
import io.swagger.models.ModelWithModelPropertyOverrides;
import io.swagger.models.ModelWithPrimitiveArray;
import io.swagger.models.ReadOnlyFields;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.models.properties.StringProperty;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ModelPropertyTest {
    @Test
    public void extractProperties() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(Family.class);
        assertEquals(models.size(), 3);

        final Model person = models.get("Person");
        final Property employer = person.getProperties().get("employer");

        assertTrue(employer instanceof ArrayProperty);
        final ArrayProperty employerProperty = (ArrayProperty) employer;

        final Property items = employerProperty.getItems();
        assertTrue(items instanceof RefProperty);
        assertEquals(((RefProperty) items).getSimpleRef(), "Employer");

        final Property awards = person.getProperties().get("awards");
        assertTrue(awards instanceof ArrayProperty);
        assertTrue(((ArrayProperty) awards).getItems() instanceof StringProperty);
    }

    @Test
    public void extractPrimitiveArray() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ModelWithPrimitiveArray.class);
        assertEquals(models.size(), 1);

        final Model model = models.get("ModelWithPrimitiveArray");
        final ArrayProperty longArray = (ArrayProperty) model.getProperties().get("longArray");
        final Property longArrayItems = longArray.getItems();
        assertTrue(longArrayItems instanceof LongProperty);

        final ArrayProperty intArray = (ArrayProperty) model.getProperties().get("intArray");
        assertTrue(intArray.getItems() instanceof IntegerProperty);
    }

    @Test
    public void readModelProperty() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(IsModelTest.class);
        final Model model = models.get("IsModelTest");
        assertNotNull(model);
    }

    @Test(description = "it should read a model with property dataTypes configured #679")
    public void readDataTypesProperty() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ModelWithModelPropertyOverrides.class);
        final String json = "{" +
                "   \"Children\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"name\":{" +
                "            \"type\":\"string\"" +
                "         }" +
                "      }" +
                "   }," +
                "   \"ModelWithModelPropertyOverrides\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"children\":{" +
                "            \"type\":\"array\"," +
                "            \"items\":{" +
                "               \"$ref\":\"#/definitions/Children\"" +
                "            }" +
                "         }" +
                "      }" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(models, json);
    }

    @Test
    public void testReadOnlyProperty() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ReadOnlyFields.class);
        ModelImpl model = (ModelImpl) models.get("ReadOnlyFields");
        assertTrue(model.getProperties().get("id").getReadOnly());
    }

    @Test
    public void modelAllowEmptyTest() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(Model1979.class);
        ModelImpl model = (ModelImpl) models.get("Model1979");
        assertTrue(model.getProperties().get("id").getAllowEmptyValue());
    }


    @Test
    public void testIssue1743() {
        final Map<String, Model> models = ModelConverters.getInstance().readAll(ModelWithBooleanProperty.class);
        final Model model = models.get("ModelWithBooleanProperty");
        assertNotNull(model);

        BooleanProperty bp = (BooleanProperty) model.getProperties().get("isGreat");
        assertTrue(bp.getEnum().size() == 1);
        assertEquals(bp.getEnum().get(0), Boolean.TRUE);
    }

    class Family {
        public Date membersSince;
        public List<Person> members;
    }

    class Person {
        public String firstname;
        public String lastname;
        public int age;
        public Date birthday;
        public List<Employer> employer;
        public List<String> awards;
    }

    class Employer {
        public String name;
        public int size;
    }

    class IsModelTest {
        public Boolean is_happy;
        public String name;
    }
}
