package io.swagger.util;


import io.swagger.models.*;
import org.testng.annotations.Test;

import java.util.Arrays;

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

        final Path path = rebuilt.getPath("/health");
        final RefPath actualPath = (RefPath) path;
        assertEquals(actualPath, expectedPath);
    }

    @Test
    public void testSerializeASpecWithResponseReferences() throws Exception {
        Swagger swagger = new Swagger()
                .host("petstore.swagger.io")
                .consumes("application/json")
                .produces("application/json");

        final RefResponse expectedResponse = new RefResponse("http://my.company.com/paths/health.json");
        swagger.path("/health", new Path().get(new Operation().response(200, expectedResponse)));

        String swaggerJson = Json.mapper().writeValueAsString(swagger);
        Swagger rebuilt = Json.mapper().readValue(swaggerJson, Swagger.class);

        assertEquals(rebuilt.getPath("/health").getGet().getResponses().get("200"), expectedResponse);

    }


    @Test
    public void testSerializeSecurityRequirement_UsingSpecCompliantMethods() throws Exception {
        SecurityRequirement securityRequirement = new SecurityRequirement().requirement("oauth2", Arrays.asList("hello", "world"));

        String json = Json.mapper().writeValueAsString(securityRequirement);
        assertEquals(json, "{\"oauth2\":[\"hello\",\"world\"]}");

        securityRequirement = new SecurityRequirement().requirement("api_key");

        json = Json.mapper().writeValueAsString(securityRequirement);
        assertEquals(json, "{\"api_key\":[]}");

        Swagger swagger = new Swagger()
                .security(new SecurityRequirement().requirement("api_key").requirement("basic_auth"))
                .security(new SecurityRequirement().requirement("oauth2", Arrays.asList("hello", "world")));
        json = Json.mapper().writeValueAsString(swagger);
        assertEquals(json, "{\"swagger\":\"2.0\",\"security\":[{\"api_key\":[],\"basic_auth\":[]},{\"oauth2\":[\"hello\",\"world\"]}]}");
    }
}
