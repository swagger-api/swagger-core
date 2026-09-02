package io.swagger.v3.oas.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * sortOutput replaces {@code Schema31Mixin} with {@code SortedSchemaMixin31}.
 * The sorted mixin must keep the scalar-type deserializer or OAS 3.1
 * {@code "type":"integer"} fails to read back into {@code Set<String>}.
 */
public class SortedSchemaMixin31TypeRoundTripTest {

    @Test
    public void sortedMixinRoundTripsScalarType() throws Exception {
        ObjectMapper mapper = Json31.mapper().copy();
        mapper.addMixIn(Schema.class, GenericOpenApiContext.SortedSchemaMixin31.class);

        JsonSchema original = new JsonSchema();
        original.setTypes(new LinkedHashSet<String>(Arrays.asList("integer")));

        String json = mapper.writeValueAsString(original);
        assertTrue(json.contains("\"type\":\"integer\"") || json.contains("\"type\": \"integer\""),
                "expected scalar type in serialized form, got: " + json);

        JsonSchema roundTripped = mapper.readValue(json, JsonSchema.class);
        assertNotNull(roundTripped);
        assertEquals(roundTripped.getTypes(), setOf("integer"));
    }

    @Test
    public void sortedMixinRoundTripsTypeArray() throws Exception {
        ObjectMapper mapper = Json31.mapper().copy();
        mapper.addMixIn(Schema.class, GenericOpenApiContext.SortedSchemaMixin31.class);

        JsonSchema original = new JsonSchema();
        original.setTypes(new LinkedHashSet<String>(Arrays.asList("string", "null")));

        JsonSchema roundTripped = mapper.readValue(mapper.writeValueAsString(original), JsonSchema.class);
        assertNotNull(roundTripped);
        assertEquals(roundTripped.getTypes(), setOf("string", "null"));
    }

    private static Set<String> setOf(String... values) {
        return new LinkedHashSet<String>(Arrays.asList(values));
    }
}
