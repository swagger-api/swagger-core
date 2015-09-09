package io.swagger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Model;
import io.swagger.models.ModelWithEnumField;
import io.swagger.models.ModelWithEnumProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;

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
    @Test(description = "it should extract enum values from method return types")
    public void testExtractEnumReturnType() {
        final Map<String, Model> models = ModelConverters.getInstance().read(ModelWithEnumProperty.class);
        final Model model = models.get("ModelWithEnumProperty");
        final Property enumProperty = model.getProperties().get("enumValue");
        assertTrue(enumProperty instanceof StringProperty);

        final StringProperty stringProperty = (StringProperty)enumProperty;
        assertEquals(stringProperty.getEnum(), Arrays.asList("PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY"));
    }
}
