package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;

import javax.xml.validation.Schema;

import org.testng.annotations.Test;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Model;
import io.swagger.models.ModelWithEnumField;
import io.swagger.models.ModelWithEnumField2707;
import io.swagger.models.ModelWithEnumProperty;
import io.swagger.models.ModelWithJacksonEnumField;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

public class EnumPropertyTest {
    @Test(description = "it should read a model with an enum property")
    public void testEnumProperty() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ModelWithEnumProperty.class);
        final String json = "{" +
                "   \"ModelWithEnumProperty\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"enumValue\":{" +
                "            \"type\":\"string\"," +
                "            \"enum\":[" +
                "               \"PRIVATE\"," +
                "               \"PUBLIC\"," +
                "               \"SYSTEM\"," +
                "               \"INVITE_ONLY\"" +
                "            ]" +
                "         }" +
                "      }" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(models, json);
    }

    @Test(description = "it should extract enum values from fields")
    public void testExtractEnumFields() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ModelWithEnumField.class);
        final Model model = models.get("ModelWithEnumField");
        final Property enumProperty = model.getProperties().get("enumValue");
        assertTrue(enumProperty instanceof StringProperty);

        final StringProperty stringProperty = (StringProperty)enumProperty;
        assertEquals(stringProperty.getEnum(), Arrays.asList("PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY"));
    }

    @Test(description = "it should extract enum values annotated with JsonProperty from fields")
    public void testExtractEnumFields2707() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ModelWithEnumField2707.class);
        final Model model = models.get("ModelWithEnumField2707");
        final Property enumProperty = model.getProperties().get("enumValue");
        assertTrue(enumProperty instanceof StringProperty);

        final StringProperty stringProperty = (StringProperty)enumProperty;
        assertEquals(stringProperty.getEnum(), Arrays.asList("private", "public"));
    }

    @Test(description = "it should extract enum values from method return types")
    public void testExtractEnumReturnType() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ModelWithEnumProperty.class);
        final Model model = models.get("ModelWithEnumProperty");
        final Property enumProperty = model.getProperties().get("enumValue");
        assertTrue(enumProperty instanceof StringProperty);

        final StringProperty stringProperty = (StringProperty)enumProperty;
        assertEquals(stringProperty.getEnum(), Arrays.asList("PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY"));
    }
    
    @Test(description = "it should extract enum values from fields using JsonProperty and JsonValue")
    public void testExtractJacksonEnumFields() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ModelWithJacksonEnumField.class);
        final Model model = models.get("ModelWithJacksonEnumField");

        final Property firstEnumProperty = (Property) model.getProperties().get("firstEnumValue");
        assertTrue(firstEnumProperty instanceof StringProperty);
        final StringProperty stringProperty = (StringProperty) firstEnumProperty;
        assertEquals(stringProperty.getEnum(), Arrays.asList("p1", "p2", "SYSTEM", "INVITE_ONLY"));

        final Property secondEnumProperty = (Property) model.getProperties().get("secondEnumValue");
        assertTrue(secondEnumProperty instanceof StringProperty);
        final StringProperty secondStringProperty = (StringProperty) secondEnumProperty;
        assertEquals(secondStringProperty.getEnum(), Arrays.asList("one", "two", "three"));

        final Property thirdEnumProperty = (Property) model.getProperties().get("thirdEnumValue");
        assertTrue(thirdEnumProperty instanceof StringProperty);
        final StringProperty thirdStringProperty = (StringProperty) thirdEnumProperty;
        assertEquals(thirdStringProperty.getEnum(), Arrays.asList("2", "4", "6"));
    }    
}
