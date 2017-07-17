package io.swagger.serialization;


import io.swagger.oas.models.Components;
import io.swagger.oas.models.OpenAPI;
import io.swagger.oas.models.Operation;
import io.swagger.oas.models.PathItem;
import io.swagger.oas.models.responses.ApiResponse;
import io.swagger.oas.models.responses.ApiResponses;
import io.swagger.oas.models.servers.Server;
import io.swagger.util.Json;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.Assert.assertEquals;

public class JsonSerializationTest {

    @Test
    public void testSerializeASpecWithPathReferences() throws Exception {

        OpenAPI swagger = new OpenAPI()
                .addServersItem(new Server().url("http://petstore.swagger.io"));

        PathItem expectedPath = new PathItem().$ref("http://my.company.com/paths/health.json");
        swagger.path("/health", expectedPath);

        String swaggerJson = Json.mapper().writeValueAsString(swagger);
        OpenAPI rebuilt = Json.mapper().readValue(swaggerJson, OpenAPI.class);

        final PathItem path = rebuilt.getPaths().get("/health");
        assertEquals(path, expectedPath);
    }


    @Test
    public void testSerializeASpecWithResponseReferences() throws Exception {
        OpenAPI swagger = new OpenAPI()
                .addServersItem(new Server().url("http://petstore.swagger.io"));

        ApiResponse expectedResponse = new ApiResponse().$ref("http://my.company.com/paths/health.json");
        PathItem expectedPath = new PathItem()
                .get(
                        new Operation().responses(
                                new ApiResponses()
                                        .addApiResponse("200", expectedResponse)));

        swagger.path("/health", expectedPath);

        String swaggerJson = Json.mapper().writeValueAsString(swagger);
        OpenAPI rebuilt = Json.mapper().readValue(swaggerJson, OpenAPI.class);

        assertEquals(rebuilt.getPaths().get("/health").getGet().getResponses().get("200"), expectedResponse);

    }

    // TODO
/*
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
*/
}
