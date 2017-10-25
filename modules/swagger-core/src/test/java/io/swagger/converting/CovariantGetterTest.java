package io.swagger.converting;

import static org.testng.Assert.assertEquals;

import io.swagger.converter.ModelConverters;
import io.swagger.matchers.SerializationMatchers;

import io.swagger.oas.models.JCovariantGetter;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Map;

public class CovariantGetterTest {
    @Test(description = "it should read a getter with covariant return type")
    public void testCovariantGetter() {
        final Map<String, Schema> models = ModelConverters.getInstance().read(JCovariantGetter.Sub.class);
        assertEquals(models.size(), 1);
        final String json = "{" +
                "   \"Sub\":{" +
                "      \"type\":\"object\"," +
                "      \"properties\":{" +
                "         \"myProperty\":{" +
                "            \"type\":\"integer\"," +
                "            \"format\":\"int32\"" +
                "         }," +
                "         \"myOtherProperty\":{" +
                "            \"type\":\"integer\"," +
                "            \"format\":\"int32\"" +
                "         }" +
                "      }" +
                "   }" +
                "}";
        SerializationMatchers.assertEqualsToJson(models, json);
    }
}
