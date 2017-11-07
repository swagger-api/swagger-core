package io.swagger.v3.core.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.TestUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.ComposedSchema;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.media.EncodingProperty;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
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

    @Test
    public void testDeserializeSecurity() throws Exception {
        final OpenAPI swagger = TestUtils.deserializeJsonFileFromClasspath("specFiles/securityDefinitions.json", OpenAPI.class);

        final List<SecurityRequirement> security = swagger.getSecurity();
        assertNotNull(security);
        assertEquals(security.size(), 3);

        final Map<String, SecurityScheme> securitySchemes = swagger.getComponents().getSecuritySchemes();
        assertNotNull(securitySchemes);
        assertEquals(securitySchemes.size(), 4);

        {
            final SecurityScheme scheme = securitySchemes.get("petstore_auth");
            assertNotNull(scheme);
            assertEquals(scheme.getType().toString(), "oauth2");
            assertEquals(scheme.getFlows().getImplicit().getAuthorizationUrl(), "http://petstore.swagger.io/oauth/dialog");
            assertEquals(scheme.getFlows().getImplicit().getScopes().get("write:pets"), "modify pets in your account");
            assertEquals(scheme.getFlows().getImplicit().getScopes().get("read:pets"), "read your pets");
        }

        {
            final SecurityScheme scheme = securitySchemes.get("api_key");
            assertNotNull(scheme);
            assertEquals(scheme.getType().toString(), "apiKey");
            assertEquals(scheme.getIn().toString(), "header");
            assertEquals(scheme.getName(), "api_key");
        }

        {
            final SecurityScheme scheme = securitySchemes.get("http");
            assertNotNull(scheme);
            assertEquals(scheme.getType().toString(), "http");
            assertEquals(scheme.getScheme(), "basic");
        }

        {
            final SecurityScheme scheme = securitySchemes.get("open_id_connect");
            assertNotNull(scheme);
            assertEquals(scheme.getType().toString(), "openIdConnect");
            assertEquals(scheme.getOpenIdConnectUrl(), "http://petstore.swagger.io/openid");
        }

        {
            final SecurityRequirement securityRequirement = security.get(0);
            final List<String> scopes = securityRequirement.get("petstore_auth");
            assertNotNull(scopes);
            assertEquals(scopes.size(), 2);
            assertTrue(scopes.contains("write:pets"));
            assertTrue(scopes.contains("read:pets"));

        }

        {
            final SecurityRequirement securityRequirement = security.get(1);
            final List<String> scopes = securityRequirement.get("api_key");
            assertNotNull(scopes);
            assertTrue(scopes.isEmpty());

        }

        {
            final SecurityRequirement securityRequirement = security.get(2);
            final List<String> scopes = securityRequirement.get("http");
            assertNotNull(scopes);
            assertTrue(scopes.isEmpty());

        }
    }

    @Test(description = "it should deserialize a Header with style")
    public void deserializeHeaderWithStyle() throws IOException {
        final String json = "{\"description\":\"aaaa\",\"style\":\"simple\"}";
        final Header p = m.readValue(json, Header.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize an Encoding with style")
    public void deserializeEncodingWithStyle() throws IOException {
        final String json = "{\"style\":\"spaceDelimited\"}";
        final Encoding p = m.readValue(json, Encoding.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should deserialize an EncodingProperty with style")
    public void deserializeEncodingPropertyWithStyle() throws IOException {
        final String json = "{\"style\":\"spaceDelimited\"}";
        final EncodingProperty p = m.readValue(json, EncodingProperty.class);
        SerializationMatchers.assertEqualsToJson(p, json);
    }

    @Test(description = "it should desserialize Long schema correctly")
    public void deserializeLongSchema() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/oas3_2.yaml");
        final OpenAPI swagger = Yaml.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        Schema s = swagger.getPaths().get("/withIntegerEnum/{stage}").getGet().getParameters().get(0).getSchema();
        assertEquals(s.getEnum().get(0), 2147483647);
        assertEquals(s.getEnum().get(1), 3147483647L);
        assertEquals(s.getEnum().get(2), 31474836475505055L);
    }
}
