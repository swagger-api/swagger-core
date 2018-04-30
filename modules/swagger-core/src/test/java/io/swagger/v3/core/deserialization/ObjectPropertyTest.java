package io.swagger.v3.core.deserialization;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ObjectPropertyTest {
    @Test(description = "convert a model with object properties")
    public void readModelWithObjectProperty() throws IOException {
        String json = "{" +
                "   \"properties\":{" +
                "      \"id\":{" +
                "         \"type\":\"string\"" +
                "      }," +
                "      \"someObject\":{" +
                "         \"type\":\"object\"," +
                "        \"x-foo\": \"vendor x\"," +
                "         \"properties\":{" +
                "            \"innerId\":{" +
                "               \"type\":\"string\"" +
                "            }" +
                "         }" +
                "      }" +
                "   }" +
                "}";

        Schema model = Json.mapper().readValue(json, Schema.class);

        Schema p = (Schema) model.getProperties().get("someObject");
        assertTrue(p instanceof ObjectSchema);

        ObjectSchema op = (ObjectSchema) p;

        Schema sp = op.getProperties().get("innerId");
        assertTrue(sp instanceof StringSchema);

        assertTrue(op.getExtensions() != null);
        assertNotNull(op.getExtensions().get("x-foo"));
        assertEquals(op.getExtensions().get("x-foo"), "vendor x");
    }
}
