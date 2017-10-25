package io.swagger.v3.core.serialization;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

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
    public void testExtension() throws Exception {

        OpenAPI swagger = new OpenAPI();
        swagger.addExtension("x-foo-bar", "foo bar");
        swagger.setInfo(new Info());
        swagger.getInfo().addExtension("x-foo-bar", "foo bar");

        String swaggerJson = Json.mapper().writeValueAsString(swagger);
        assertFalse(swaggerJson.contains("extensions"));
        OpenAPI rebuilt = Json.mapper().readValue(swaggerJson, OpenAPI.class);
        assertEquals(rebuilt.getExtensions().values().iterator().next(), "foo bar");
        assertEquals(rebuilt.getInfo().getExtensions().values().iterator().next(), "foo bar");

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
}
