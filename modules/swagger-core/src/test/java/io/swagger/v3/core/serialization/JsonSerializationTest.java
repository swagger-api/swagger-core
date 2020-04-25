package io.swagger.v3.core.serialization;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
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

    @Test(description = "only serialize appropriate fields for SecurityScheme of type APIKEY")
    public void testSerializeSecuritySchemeApiKey() {
        SecurityScheme scheme = makeSecurityScheme(SecurityScheme.Type.APIKEY);
        assertEquals("{\n" +
                "  \"type\" : \"apiKey\",\n" +
                "  \"description\" : \"description\",\n" +
                "  \"name\" : \"name\",\n" +
                "  \"in\" : \"header\"\n" +
                "}", Json.pretty(scheme));
    }

    @Test(description = "only serialize appropriate fields for SecurityScheme of type HTTP")
    public void testSerializeSecuritySchemeHttp() {
        SecurityScheme scheme = makeSecurityScheme(SecurityScheme.Type.HTTP);
        assertEquals("{\n" +
                "  \"type\" : \"http\",\n" +
                "  \"description\" : \"description\",\n" +
                "  \"scheme\" : \"scheme\",\n" +
                "  \"bearerFormat\" : \"bearerFormat\"\n" +
                "}", Json.pretty(scheme));
    }

    @Test(description = "only serialize appropriate fields for SecurityScheme of type OAUTH2")
    public void testSerializeSecuritySchemeOAuth2() {
        SecurityScheme scheme = makeSecurityScheme(SecurityScheme.Type.OAUTH2);
        assertEquals("{\n" +
                "  \"type\" : \"oauth2\",\n" +
                "  \"description\" : \"description\",\n" +
                "  \"flows\" : { }\n" +
                "}", Json.pretty(scheme));
    }

    @Test(description = "only serialize appropriate fields for SecurityScheme of type OPENIDCONNECT")
    public void testSerializeSecuritySchemeOpenIdConnect() {
        SecurityScheme scheme = makeSecurityScheme(SecurityScheme.Type.OPENIDCONNECT);
        assertEquals("{\n" +
                "  \"type\" : \"openIdConnect\",\n" +
                "  \"description\" : \"description\",\n" +
                "  \"openIdConnectUrl\" : \"openIdConnectUrl\"\n" +
                "}", Json.pretty(scheme));
    }

    private SecurityScheme makeSecurityScheme(SecurityScheme.Type type) {
        return new SecurityScheme()
                .type(type)
                .description("description")
                .name("name")
                .in(SecurityScheme.In.HEADER)
                .scheme("scheme")
                .bearerFormat("bearerFormat")
                .flows(new OAuthFlows())
                .openIdConnectUrl("openIdConnectUrl");
    }
}
