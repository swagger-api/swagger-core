package io.swagger.util;

import io.swagger.models.*;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class JsonDeserializationTest {

    @Test
    public void testDeserializePetStoreFile() throws Exception {
        deserializeFile("src/test/scala/specFiles/petstore.json");
    }

    @Test
    public void testDeserializeCompositionTest() throws Exception {
        deserializeFile("src/test/scala/specFiles/compositionTest.json");
    }

    @Test
    public void testDeserializeAPathRef() throws Exception {
        final Swagger swagger = deserializeFile("src/test/scala/specFiles/pathRef.json");

        final Path petPath = swagger.getPath("/pet");
        assertTrue(petPath instanceof RefPath);
        assertEquals(((RefPath) petPath).get$ref(), "http://my.company.com/paths/health.json");
        assertTrue(swagger.getPath("/user") instanceof PathImpl);
    }

    @Test
    public void testDeserializeAResponseRef() throws Exception {
        final Swagger swagger = deserializeFile("src/test/scala/specFiles/responseRef.json");

        final Map<String, Response> responseMap = swagger.getPath("/pet").getPut().getResponses();

        assertIsRefResponse(responseMap.get("405"), "http://my.company.com/responses/errors.json#/method-not-allowed");
        assertIsRefResponse(responseMap.get("404"), "http://my.company.com/responses/errors.json#/not-found");
        assertTrue(responseMap.get("400") instanceof ResponseImpl);
    }

    private void assertIsRefResponse(Response response, String expectedRef) {
        assertTrue(response instanceof RefResponse);

        RefResponse refResponse = (RefResponse) response;
        assertEquals(refResponse.get$ref(), expectedRef);
    }

    private Swagger deserializeFile(String pathname) throws java.io.IOException {
        return Json.mapper().readValue(new File(pathname), Swagger.class);
    }
}
