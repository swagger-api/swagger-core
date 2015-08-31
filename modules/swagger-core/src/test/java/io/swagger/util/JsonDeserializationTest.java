package io.swagger.util;

import io.swagger.TestUtils;
import io.swagger.models.*;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;


public class JsonDeserializationTest {

    @Test
    public void testDeserializePetStoreFile() throws Exception {
        TestUtils.deserializeJsonFileFromClasspath("specFiles/petstore.json", Swagger.class);
    }

    @Test
    public void testDeserializeCompositionTest() throws Exception {
        TestUtils.deserializeJsonFileFromClasspath("specFiles/compositionTest.json", Swagger.class);
    }

    @Test
    public void testDeserializeAPathRef() throws Exception {
        final Swagger swagger = TestUtils.deserializeJsonFileFromClasspath("specFiles/pathRef.json", Swagger.class);

        final Path petPath = swagger.getPath("/pet");
        assertTrue(petPath instanceof RefPath);
        assertEquals(((RefPath) petPath).get$ref(), "http://my.company.com/paths/health.json");
        assertTrue(swagger.getPath("/user") instanceof Path);
    }

    @Test
    public void testDeserializeAResponseRef() throws Exception {
        final Swagger swagger = TestUtils.deserializeJsonFileFromClasspath("specFiles/responseRef.json", Swagger.class);

        final Map<String, Response> responseMap = swagger.getPath("/pet").getPut().getResponses();

        assertIsRefResponse(responseMap.get("405"), "http://my.company.com/responses/errors.json#/method-not-allowed");
        assertIsRefResponse(responseMap.get("404"), "http://my.company.com/responses/errors.json#/not-found");
        assertTrue(responseMap.get("400") instanceof Response);
    }

    private void assertIsRefResponse(Response response, String expectedRef) {
        assertTrue(response instanceof RefResponse);

        RefResponse refResponse = (RefResponse) response;
        assertEquals(refResponse.get$ref(), expectedRef);
    }

    @Test
    public void testDeserializeSecurityRequirement() throws Exception {
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

    @Test (description = "should deserialize a property with vendor extensions of different types")
    public void testDeserializePropertyWithVendorExtensions() throws Exception {

        Swagger swagger = TestUtils.deserializeJsonFileFromClasspath("specFiles/propertyWithVendorExtensions.json", Swagger.class);

        Map<String, Object> vendorExtensions = swagger.getDefinitions().get("Health").getProperties().get("status").getVendorExtensions();

        assertNotNull(vendorExtensions);
        assertEquals(6, vendorExtensions.size());

        String xStringValue = (String) vendorExtensions.get("x-string-value");
        assertNotNull(xStringValue);
        assertEquals(xStringValue, "Hello World");

        assertTrue(vendorExtensions.containsKey("x-null-value"));
        assertNull(vendorExtensions.get("x-null-value"));

        Map<String, String> xMapValue = (Map) vendorExtensions.get("x-map-value");
        assertNotNull(xMapValue);
        assertEquals(xMapValue.get("hello"), "world");
        assertEquals(xMapValue.get("foo"), "bar");

        List<String> xListValue = (List) vendorExtensions.get("x-list-value");
        assertNotNull(xListValue);
        assertEquals(xListValue.get(0), "Hello");
        assertEquals(xListValue.get(1), "World");

        Integer xNumberValue = (Integer) vendorExtensions.get("x-number-value");
        assertNotNull(xNumberValue);
        assertEquals(xNumberValue.intValue(), 123);

        Boolean xBooleanValue = (Boolean) vendorExtensions.get("x-boolean-value");
        assertNotNull(xBooleanValue);
        assertTrue(xBooleanValue);

        assertFalse(vendorExtensions.containsKey("not-an-extension"));

        //check for vendor extensions in array property types
        vendorExtensions = swagger.getDefinitions().get("Health").getProperties().get("array").getVendorExtensions();

        xStringValue = (String) vendorExtensions.get("x-string-value");
        assertNotNull(xStringValue);
        assertEquals(xStringValue, "string_value");
    }
}
