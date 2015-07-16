package io.swagger.util;


import io.swagger.models.*;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class JsonSerializationTest {

    @Test
    public void testSerializeASpecWithPathReferences() throws Exception {

        Swagger swagger = new Swagger()
                .host("petstore.swagger.io")
                .consumes("application/json")
                .produces("application/json");


        final RefPath expectedPath = new RefPath("http://my.company.com/paths/health.json");
        swagger.path("/health", expectedPath);

        String swaggerJson = Json.mapper().writeValueAsString(swagger);
        Swagger rebuilt = Json.mapper().readValue(swaggerJson, Swagger.class);

        assertEquals(rebuilt.getPath("/health"), expectedPath);
    }

    @Test
    public void testSerializeASpecWithResponseReferences() throws Exception {
        Swagger swagger = new Swagger()
                .host("petstore.swagger.io")
                .consumes("application/json")
                .produces("application/json");

        final RefResponse expectedResponse = new RefResponse("http://my.company.com/paths/health.json");
        swagger.path("/health", new PathImpl().get(new Operation().response(200, expectedResponse)));

        String swaggerJson = Json.mapper().writeValueAsString(swagger);
        Swagger rebuilt = Json.mapper().readValue(swaggerJson, Swagger.class);

        assertEquals(rebuilt.getPath("/health").getGet().getResponses().get("200"), expectedResponse);

    }
}
