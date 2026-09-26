package io.swagger.v3.core.deserialization;

import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.models.media.JsonSchema;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * OpenAPI 3.1 serializes a single schema type as a JSON string
 * ({@code "type":"integer"}) via {@code Schema31Mixin.TypeSerializer}, while
 * the model field is {@code Set<String> types}. Without a matching deserializer,
 * round-tripping {@link JsonSchema} fails when reading that scalar back.
 *
 * @see <a href="https://github.com/swagger-api/swagger-core/issues/5264">#5264</a>
 */
public class JsonSchemaTypeRoundTripTest {

    @Test
    public void singleTypeStringRoundTripsOnJsonSchema() throws Exception {
        JsonSchema original = new JsonSchema();
        original.setTypes(new LinkedHashSet<>(Arrays.asList("integer")));

        String json = Json31.mapper().writeValueAsString(original);
        assertTrue(json.contains("\"type\":\"integer\"") || json.contains("\"type\": \"integer\""),
                "expected scalar type in serialized form, got: " + json);

        JsonSchema roundTripped = Json31.mapper().readValue(json, JsonSchema.class);
        assertNotNull(roundTripped);
        assertEquals(roundTripped.getTypes(), setOf("integer"));
    }

    @Test
    public void multiTypeArrayRoundTripsOnJsonSchema() throws Exception {
        JsonSchema original = new JsonSchema();
        original.setTypes(new LinkedHashSet<>(Arrays.asList("string", "null")));

        String json = Json31.mapper().writeValueAsString(original);
        JsonSchema roundTripped = Json31.mapper().readValue(json, JsonSchema.class);

        assertNotNull(roundTripped);
        assertEquals(roundTripped.getTypes(), setOf("string", "null"));
    }

    @Test
    public void convertValuePreservesSingleType() {
        JsonSchema original = new JsonSchema();
        original.setTypes(new LinkedHashSet<>(Arrays.asList("array")));

        JsonSchema converted = Json31.mapper().convertValue(original, JsonSchema.class);
        assertNotNull(converted);
        assertEquals(converted.getTypes(), setOf("array"));
    }

    private static Set<String> setOf(String... values) {
        return new LinkedHashSet<>(Arrays.asList(values));
    }
}
