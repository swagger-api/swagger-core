package io.swagger.v3.core.serialization;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;
import tools.jackson.core.JsonParser;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.databind.ObjectMapper;

import static org.testng.Assert.assertNotNull;

/**
 * Reproducer for Bug 7: objectReadContext().readTree() throws
 * UnsupportedOperationException when the parser is created externally
 * (not by ObjectMapper itself).
 */
public class ExternalParserDeserializationTest {

    private static final String MINIMAL_SPEC = """
            {
              "openapi": "3.0.3",
              "info": { "title": "Test", "version": "1.0" },
              "paths": {}
            }
            """;

    @Test(description = "Bug 7: readValue with externally-created parser must not throw UOE")
    public void testExternalJsonParserDeserialization() {
        ObjectMapper mapper = Json.mapper();
        JsonParser parser = new JsonFactory().createParser(MINIMAL_SPEC);
        OpenAPI result = mapper.readValue(parser, OpenAPI.class);
        assertNotNull(result);
        assertNotNull(result.getInfo());
    }
}
