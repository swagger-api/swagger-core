package io.swagger.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.util.TestUtils;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.media.ComposedSchema;
import io.swagger.oas.models.media.Schema;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.oas.models.responses.ApiResponses;
import io.swagger.util.Json;
import io.swagger.util.ResourceUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class JsonDeserializationTest {
    private final ObjectMapper m = Json.mapper();

    @Test(description = "it should deserialize the petstore")
    public void testPetstore() throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), "specFiles/petstore-3.0.json");
        final Object swagger = m.readValue(json, OpenAPI.class);
        assertTrue(swagger instanceof OpenAPI);
    }

    @Test(description = "it should deserialize the composition test")
    public void testCompositionTest() throws IOException {
        final String json = ResourceUtils.loadClassResource(getClass(), "specFiles/compositionTest-3.0.json");
        final Object deserialized = m.readValue(json, OpenAPI.class);
        assertTrue(deserialized instanceof OpenAPI);
        OpenAPI openAPI = (OpenAPI)deserialized;
        Schema lizardSchema = openAPI.getComponents().getSchemas().get("Lizard");
        assertTrue(lizardSchema instanceof ComposedSchema);
        assertEquals(((ComposedSchema)lizardSchema).getAllOf().size(), 2);

        Schema petSchema = openAPI.getComponents().getSchemas().get("Pet");
        assertEquals(petSchema.getDiscriminator().getPropertyName(), "pet_type");
        assertEquals(petSchema.getDiscriminator().getMapping().get("cachorro"), "#/components/schemas/Dog");

    }

    @Test(description = "it should deserialize a simple ObjectProperty")
    public void testObjectProperty() throws IOException {
        final String json = "{\n" +
                "   \"type\":\"object\",\n" +
                "   \"title\":\"objectProperty\",\n" +
                "   \"description\":\"top level object\",\n" +
                "   \"properties\":{\n" +
                "      \"property1\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"description\":\"First property\"\n" +
                "      },\n" +
                "      \"property2\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"description\":\"Second property\"\n" +
                "      },\n" +
                "      \"property3\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"description\":\"Third property\"\n" +
                "      }\n" +
                "   }\n" +
                "}";
        final Schema result = m.readValue(json, Schema.class);
        assertEquals(3, result.getProperties().size());
        assertEquals("objectProperty", result.getTitle());
    }

    @Test(description = "it should deserialize nested ObjectProperty(s)")
    public void testNestedObjectProperty() throws IOException {
        final String json = "{\n" +
                "   \"type\":\"object\",\n" +
                "   \"description\":\"top level object\",\n" +
                "   \"properties\":{\n" +
                "      \"property1\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"description\":\"First property\"\n" +
                "      },\n" +
                "      \"property2\":{\n" +
                "         \"type\":\"string\",\n" +
                "         \"description\":\"Second property\"\n" +
                "      },\n" +
                "      \"property3\":{\n" +
                "         \"type\":\"object\",\n" +
                "         \"description\":\"Third property\",\n" +
                "         \"properties\":{\n" +
                "            \"property1\":{\n" +
                "               \"type\":\"string\",\n" +
                "               \"description\":\"First nested property\"\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "   }\n" +
                "}";
        final Schema result = m.readValue(json, Schema.class);
        final Map<String, Schema> firstLevelProperties = result.getProperties();
        assertEquals(firstLevelProperties.size(), 3);

        final Schema property3 = firstLevelProperties.get("property3");

        final Map<String, Schema> secondLevelProperties = property3.getProperties();
        assertEquals(secondLevelProperties.size(), 1);
    }

    @Test
    public void testDeserializePetStoreFile() throws Exception {
        TestUtils.deserializeJsonFileFromClasspath("specFiles/petstore.json", OpenAPI.class);
    }

    @Test
    public void testDeserializeCompositionTest() throws Exception {
        TestUtils.deserializeJsonFileFromClasspath("specFiles/compositionTest.json", OpenAPI.class);
    }

    @Test
    public void testDeserializeAPathRef() throws Exception {
        final OpenAPI oas = TestUtils.deserializeJsonFileFromClasspath("specFiles/pathRef.json", OpenAPI.class);

        final PathItem petPath = oas.getPaths().get("/pet");
        assertNotNull(petPath.get$ref());
        assertEquals(petPath.get$ref(), "http://my.company.com/paths/health.json");
        assertTrue(oas.getPaths().get("/user") instanceof PathItem);
    }

    @Test
    public void testDeserializeAResponseRef() throws Exception {
        final OpenAPI oas = TestUtils.deserializeJsonFileFromClasspath("specFiles/responseRef.json", OpenAPI.class);

        final ApiResponses responseMap = oas.getPaths().get("/pet").getPut().getResponses();

        // TODO: missing response ref
        assertIsRefResponse(responseMap.get("405"), "http://my.company.com/responses/errors.json#/method-not-allowed");
        assertIsRefResponse(responseMap.get("404"), "http://my.company.com/responses/errors.json#/not-found");
        assertTrue(responseMap.get("400") instanceof ApiResponse);
    }

    private void assertIsRefResponse(Object response, String expectedRef) {
        assertTrue(response instanceof ApiResponse);

        ApiResponse refResponse = (ApiResponse) response;
        assertEquals(refResponse.get$ref(), expectedRef);
    }

    // TODO
/*
    @Test
    public void testDeserializeSecurity() throws Exception {
        final Swagger swagger = TestUtils.deserializeJsonFileFromClasspath("specFiles/securityDefinitions.json", Swagger.class);

        final List<SecurityRequirement> security = swagger.getSecurity();
        final List<SecurityRequirement> securityRequirements = swagger.getSecurityRequirement();
        assertNotNull(security);
        assertEquals(security, securityRequirements);

        assertEquals(security.size(), 2);

        {
            final SecurityRequirement securityRequirement = security.get(0);
            final Map<String, List<String>> requirements = securityRequirement.getRequirements();
            final List<String> basic_auth = requirements.get("basic_auth");
            assertNotNull(basic_auth);
            assertTrue(basic_auth.isEmpty());

            final List<String> api_key = requirements.get("api_key");
            assertNotNull(api_key);
            assertTrue(api_key.isEmpty());
        }

        {
            final SecurityRequirement securityRequirement = security.get(1);
            final Map<String, List<String>> requirements = securityRequirement.getRequirements();
            final List<String> oauth2 = requirements.get("oauth2");
            assertNotNull(oauth2);
            assertFalse(oauth2.isEmpty());

            assertEquals(oauth2.get(0), "hello");
            assertEquals(oauth2.get(1), "world");
        }
    }
*/

}
