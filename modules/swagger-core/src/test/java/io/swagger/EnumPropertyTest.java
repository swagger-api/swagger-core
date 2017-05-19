package io.swagger;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.oas.models.ModelWithEnumField;
import io.swagger.oas.models.ModelWithEnumProperty;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class EnumPropertyTest {
    @Test(description = "it should read a model with an enum property")
    public void testEnumProperty() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithEnumProperty.class);
        final String json = "{" +
                "   \"ModelWithEnumProperty\":{" +
                "   \"title\":\"ModelWithEnumProperty\"," +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"enumValue\":{" +
                "         \"title\":\"enumValue\"," +
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
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithEnumField.class);
        final Schema model = models.get("ModelWithEnumField");
        final Schema enumProperty = (Schema)model.getProperties().get("enumValue");
        assertTrue(enumProperty instanceof StringSchema);

        final StringSchema stringProperty = (StringSchema)enumProperty;
        assertEquals(stringProperty.getEnum(), Arrays.asList("PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY"));
    }
    @Test(description = "it should extract enum values from method return types")
    public void testExtractEnumReturnType() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(ModelWithEnumProperty.class);
        final Schema model = models.get("ModelWithEnumProperty");
        final Schema enumProperty = (Schema)model.getProperties().get("enumValue");
        assertTrue(enumProperty instanceof StringSchema);

        final StringSchema stringProperty = (StringSchema)enumProperty;
        assertEquals(stringProperty.getEnum(), Arrays.asList("PRIVATE", "PUBLIC", "SYSTEM", "INVITE_ONLY"));
    }
}
