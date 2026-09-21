package io.swagger.v3.core.serialization;

import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.filter.resources.NoOpOperationsFilter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Json32;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.core.util.Yaml32;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.XML;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class OpenAPI3_2SerializationTest {

    private OpenAPI buildDoc() {
        Operation queryOp = new Operation()
                .operationId("queryPets")
                .responses(new ApiResponses().addApiResponse("200",
                        new ApiResponse().description("query result")));
        PathItem item = new PathItem().query(queryOp);
        Paths paths = new Paths();
        paths.addPathItem("/pets", item);
        return new OpenAPI()
                .openapi("3.2.0")
                .info(new Info().title("t").version("1"))
                .paths(paths);
    }

    @Test
    public void querySerializesIn32Json() throws Exception {
        String out = Json32.mapper().writeValueAsString(buildDoc());
        assertTrue(out.contains("\"query\":"), "3.2 JSON output should contain 'query': " + out);
        assertTrue(out.contains("queryPets"), out);
    }

    @Test
    public void querySerializesIn32Yaml() throws Exception {
        String out = Yaml32.mapper().writeValueAsString(buildDoc());
        assertTrue(out.contains("query:"), "3.2 YAML output should contain 'query:': " + out);
        assertTrue(out.contains("queryPets"), out);
    }

    @Test
    public void queryHiddenIn30And31() throws Exception {
        String out30 = Json.mapper().writeValueAsString(buildDoc());
        assertFalse(out30.contains("\"query\""), "3.0 output must not contain 'query': " + out30);
        String out31 = Json31.mapper().writeValueAsString(buildDoc());
        assertFalse(out31.contains("\"query\""), "3.1 output must not contain 'query': " + out31);
    }

    @Test
    public void queryRoundTrip32() throws Exception {
        OpenAPI doc = buildDoc();
        String out = Json32.mapper().writeValueAsString(doc);
        OpenAPI readBack = Json32.mapper().readValue(out, OpenAPI.class);
        assertNotNull(readBack.getPaths().get("/pets").getQuery());
        assertEquals(readBack.getPaths().get("/pets").getQuery().getOperationId(), "queryPets");
    }

    @Test
    public void queryDeserializesFromDocument() throws Exception {
        String doc = "openapi: 3.2.0\n" +
                "info:\n" +
                "  title: t\n" +
                "  version: '1'\n" +
                "paths:\n" +
                "  /pets:\n" +
                "    query:\n" +
                "      operationId: queryPets\n" +
                "      responses:\n" +
                "        '200':\n" +
                "          description: query result\n";
        OpenAPI readBack = Yaml32.mapper().readValue(doc, OpenAPI.class);
        PathItem item = readBack.getPaths().get("/pets");
        assertNotNull(item);
        assertNotNull(item.getQuery());
        assertEquals(item.getQuery().getOperationId(), "queryPets");
    }

    @Test
    public void queryIgnoredIn31Deserialization() throws Exception {
        // 3.1 mapper must not surface 'query' even if present in input
        String doc = "openapi: 3.1.0\n" +
                "info:\n" +
                "  title: t\n" +
                "  version: '1'\n" +
                "paths:\n" +
                "  /pets:\n" +
                "    query:\n" +
                "      operationId: queryPets\n" +
                "      responses:\n" +
                "        '200':\n" +
                "          description: query result\n";
        OpenAPI readBack = Yaml31.mapper().readValue(doc, OpenAPI.class);
        PathItem item = readBack.getPaths().get("/pets");
        assertNotNull(item);
        assertNull(item.getQuery(), "3.1 mapper must not bind 'query'");
    }

    @Test
    public void queryBindsInNestedPathItems32() throws Exception {
        String doc = "openapi: 3.2.0\n" +
                "info:\n" +
                "  title: t\n" +
                "  version: '1'\n" +
                "webhooks:\n" +
                "  petEvent:\n" +
                "    query:\n" +
                "      operationId: webhookQuery\n" +
                "      responses:\n" +
                "        '200':\n" +
                "          description: ok\n" +
                "components:\n" +
                "  pathItems:\n" +
                "    shared:\n" +
                "      query:\n" +
                "        operationId: sharedQuery\n" +
                "        responses:\n" +
                "          '200':\n" +
                "            description: ok\n";
        OpenAPI readBack = Yaml32.mapper().readValue(doc, OpenAPI.class);
        assertNotNull(readBack.getWebhooks().get("petEvent").getQuery(), "webhooks PathItem must bind 'query'");
        assertEquals(readBack.getWebhooks().get("petEvent").getQuery().getOperationId(), "webhookQuery");
        assertNotNull(readBack.getComponents().getPathItems().get("shared").getQuery(), "components.pathItems must bind 'query'");
        assertEquals(readBack.getComponents().getPathItems().get("shared").getQuery().getOperationId(), "sharedQuery");
    }

    @Test
    public void specVersionIsV32() throws Exception {
        String doc = "openapi: 3.2.0\ninfo:\n  title: t\n  version: '1'\npaths: {}\n";
        OpenAPI readBack = Yaml32.mapper().readValue(doc, OpenAPI.class);
        assertEquals(readBack.getSpecVersion(), io.swagger.v3.oas.models.SpecVersion.V32);
    }

    @Test
    public void queryBindsInsideCallback32() throws Exception {
        String doc = "openapi: 3.2.0\n" +
                "info:\n" +
                "  title: t\n" +
                "  version: '1'\n" +
                "paths:\n" +
                "  /pets:\n" +
                "    get:\n" +
                "      operationId: getPets\n" +
                "      callbacks:\n" +
                "        onData:\n" +
                "          '{$request.body#/cbUrl}':\n" +
                "            query:\n" +
                "              operationId: callbackQuery\n" +
                "              responses:\n" +
                "                '200':\n" +
                "                  description: ok\n" +
                "      responses:\n" +
                "        '200':\n" +
                "          description: ok\n";
        OpenAPI readBack = Yaml32.mapper().readValue(doc, OpenAPI.class);
        PathItem callbackItem = readBack.getPaths().get("/pets").getGet()
                .getCallbacks().get("onData").get("{$request.body#/cbUrl}");
        assertNotNull(callbackItem);
        assertNotNull(callbackItem.getQuery(), "callback PathItem must bind 'query'");
        assertEquals(callbackItem.getQuery().getOperationId(), "callbackQuery");
    }

    @Test
    public void queryRoundTrip32Yaml() throws Exception {
        OpenAPI doc = buildDoc();
        String out = Yaml32.mapper().writeValueAsString(doc);
        OpenAPI readBack = Yaml32.mapper().readValue(out, OpenAPI.class);
        assertNotNull(readBack.getPaths().get("/pets").getQuery());
        assertEquals(readBack.getPaths().get("/pets").getQuery().getOperationId(), "queryPets");
        assertEquals(readBack.getSpecVersion(), SpecVersion.V32);
    }

    @Test
    public void v32DocumentBindsStandardModels() throws Exception {
        // Coverage: a 3.2 document parses end-to-end through the V32 pipeline and binds
        // standard models (parameters, responses with content, schemas, securitySchemes).
        // Note: these model shapes parse identically under the 3.1 and 3.2 mappers, so
        // this test does not prove V32-specific binding semantics on its own; the
        // V32-only observable behavior is covered by the 'query' PathItem tests above
        // and by specVersionIsV32.
        String doc = "openapi: 3.2.0\n" +
                "info:\n" +
                "  title: t\n" +
                "  version: '1'\n" +
                "paths:\n" +
                "  /pets:\n" +
                "    get:\n" +
                "      operationId: getPets\n" +
                "      parameters:\n" +
                "        - name: limit\n" +
                "          in: query\n" +
                "          schema:\n" +
                "            type: integer\n" +
                "      responses:\n" +
                "        '200':\n" +
                "          description: pet list\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                type: array\n" +
                "                items:\n" +
                "                  type: string\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Pet:\n" +
                "      type:\n" +
                "        - string\n" +
                "        - 'null'\n" +
                "  securitySchemes:\n" +
                "    bearerAuth:\n" +
                "      type: http\n" +
                "      scheme: bearer\n";
        OpenAPI readBack = Yaml32.mapper().readValue(doc, OpenAPI.class);
        assertNotNull(readBack);

        Parameter p = readBack.getPaths().get("/pets").getGet().getParameters().get(0);
        assertNotNull(p);
        assertEquals(p.getName(), "limit");
        assertEquals(p.getIn(), "query");

        ApiResponse response = readBack.getPaths().get("/pets").getGet().getResponses().get("200");
        assertNotNull(response);
        assertEquals(response.getDescription(), "pet list");
        assertNotNull(response.getContent());
        assertNotNull(response.getContent().get("application/json"));
        assertNotNull(response.getContent().get("application/json").getSchema());

        Schema pet = readBack.getComponents().getSchemas().get("Pet");
        assertNotNull(pet);
        assertNotNull(pet.getTypes(), "JSON-Schema-style types array must bind through the V32 model deserializer");
        assertTrue(pet.getTypes().contains("string") && pet.getTypes().contains("null"),
                "types must contain string and null: " + pet.getTypes());

        SecurityScheme scheme = readBack.getComponents().getSecuritySchemes().get("bearerAuth");
        assertNotNull(scheme);
        assertEquals(scheme.getType(), SecurityScheme.Type.HTTP);
        assertEquals(scheme.getScheme(), "bearer");
    }

    @Test
    public void toStringIncludesWebhooksAndDialectForV32() {
        OpenAPI doc = buildDoc();
        doc.setSpecVersion(SpecVersion.V32);
        doc.setJsonSchemaDialect("https://json-schema.org/draft/2020-12/schema");
        Map<String, PathItem> hooks = new LinkedHashMap<>();
        hooks.put("petEvent", new PathItem());
        doc.setWebhooks(hooks);

        String s = doc.toString();
        assertTrue(s.contains("webhooks:"), "V32 toString must print webhooks: " + s);
        assertTrue(s.contains("jsonSchemaDialect:"), "V32 toString must print jsonSchemaDialect: " + s);

        doc.setSpecVersion(SpecVersion.V30);
        String s30 = doc.toString();
        assertFalse(s30.contains("webhooks:"), "V30 toString must not print webhooks");
        assertFalse(s30.contains("jsonSchemaDialect:"), "V30 toString must not print jsonSchemaDialect");
    }

    // ---- issue #9: simple fixed-field additions ----

    private OpenAPI buildIssue9Doc() {
        return new OpenAPI()
                .openapi("3.2.0")
                .info(new Info().title("t").version("1"))
                .addServersItem(new Server().url("https://api.example.com").name("production"))
                .addTagsItem(new Tag().name("pets").summary("Pet operations").parent("animals").kind("nav"))
                .paths(new Paths().addPathItem("/pets", new PathItem().get(
                        new Operation().operationId("getPets").responses(
                                new ApiResponses().addApiResponse("200",
                                        new ApiResponse().description("ok").summary("pet list")
                                                .content(new Content().addMediaType("application/json",
                                                        new MediaType().itemSchema(new Schema().typesItem("string")))))))))
                .components(new Components()
                        .addExamples("dataEx", new Example().summary("s").dataValue(java.util.Collections.singletonMap("a", 1)))
                        .addExamples("serEx", new Example().serializedValue("{\"a\":1}"))
                        .addSecuritySchemes("oauth", new SecurityScheme()
                                .type(SecurityScheme.Type.OAUTH2)
                                .deprecated(true)
                                .oauth2MetadataUrl("https://auth.example.com/.well-known/oauth-authorization-server")
                                .flows(new OAuthFlows()
                                        .deviceAuthorization(
                                                new OAuthFlow().deviceAuthorizationUrl("https://auth.example.com/device")
                                                        .tokenUrl("https://auth.example.com/token"))
                                        // authorizationCode binds in all versions; it exercises the
                                        // OAuthFlow mixin hiding deviceAuthorizationUrl on its own
                                        .authorizationCode(
                                                new OAuthFlow().authorizationUrl("https://auth.example.com/authorize")
                                                        .tokenUrl("https://auth.example.com/token")
                                                        .deviceAuthorizationUrl("https://auth.example.com/device"))))
                        .addSchemas("Pet", new Schema()
                                .type("object")
                                .discriminator(new Discriminator().propertyName("petType").defaultMapping("Dog"))
                                .xml(new XML().nodeType("element"))));
    }

    @Test
    public void newFields32SerializeAndRoundTrip() throws Exception {
        String out = Json32.mapper().writeValueAsString(buildIssue9Doc());
        for (String token : new String[]{
                "\"name\":\"production\"",
                "\"summary\":\"Pet operations\"",
                "\"parent\":\"animals\"",
                "\"kind\":\"nav\"",
                "\"summary\":\"pet list\"",
                "\"itemSchema\":",
                "\"dataValue\":",
                "\"serializedValue\":\"{\\\"a\\\":1}\"",
                "\"deprecated\":true",
                "\"oauth2MetadataUrl\":",
                "\"deviceAuthorization\":",
                "\"deviceAuthorizationUrl\":",
                "\"defaultMapping\":\"Dog\"",
                "\"nodeType\":\"element\""}) {
            assertTrue(out.contains(token), "3.2 JSON output must contain " + token + " : " + out);
        }

        OpenAPI readBack = Json32.mapper().readValue(out, OpenAPI.class);
        assertEquals(readBack.getServers().get(0).getName(), "production");
        Tag tag = readBack.getTags().get(0);
        assertEquals(tag.getSummary(), "Pet operations");
        assertEquals(tag.getParent(), "animals");
        assertEquals(tag.getKind(), "nav");
        ApiResponse resp = readBack.getPaths().get("/pets").getGet().getResponses().get("200");
        assertEquals(resp.getSummary(), "pet list");
        // 3.1-family schema deserialization normalizes `type` into the `types` set
        assertTrue(resp.getContent().get("application/json").getItemSchema().getTypes().contains("string"));
        assertNotNull(readBack.getComponents().getExamples().get("dataEx").getDataValue());
        assertEquals(readBack.getComponents().getExamples().get("serEx").getSerializedValue(), "{\"a\":1}");
        SecurityScheme scheme = readBack.getComponents().getSecuritySchemes().get("oauth");
        assertEquals(scheme.getDeprecated(), Boolean.TRUE);
        assertNotNull(scheme.getOauth2MetadataUrl());
        assertEquals(scheme.getFlows().getDeviceAuthorization().getDeviceAuthorizationUrl(),
                "https://auth.example.com/device");
        assertEquals(scheme.getFlows().getAuthorizationCode().getDeviceAuthorizationUrl(),
                "https://auth.example.com/device");
        Schema pet = readBack.getComponents().getSchemas().get("Pet");
        assertEquals(pet.getDiscriminator().getDefaultMapping(), "Dog");
        assertEquals(pet.getXml().getNodeType(), "element");
    }

    @Test
    public void newFields32HiddenIn30And31() throws Exception {
        OpenAPI doc = buildIssue9Doc();
        for (com.fasterxml.jackson.databind.ObjectMapper m :
                new com.fasterxml.jackson.databind.ObjectMapper[]{Json.mapper(), Json31.mapper()}) {
            String out = m.writeValueAsString(doc);
            assertFalse(out.contains("production"), "must not emit Server.name: " + out);
            assertFalse(out.contains("Pet operations"), "must not emit Tag.summary: " + out);
            assertFalse(out.contains("animals"), "must not emit Tag.parent: " + out);
            assertFalse(out.contains("nav"), "must not emit Tag.kind: " + out);
            assertFalse(out.contains("pet list"), "must not emit Response.summary: " + out);
            assertFalse(out.contains("itemSchema"), "must not emit MediaType.itemSchema: " + out);
            assertFalse(out.contains("dataValue"), "must not emit Example.dataValue: " + out);
            assertFalse(out.contains("serializedValue"), "must not emit Example.serializedValue: " + out);
            assertFalse(out.contains("\"deprecated\":true"), "must not emit SecurityScheme.deprecated: " + out);
            assertFalse(out.contains("oauth2MetadataUrl"), "must not emit SecurityScheme.oauth2MetadataUrl: " + out);
            assertFalse(out.contains("deviceAuthorization"), "must not emit OAuthFlows.deviceAuthorization: " + out);
            assertFalse(out.contains("defaultMapping"), "must not emit Discriminator.defaultMapping: " + out);
            assertFalse(out.contains("nodeType"), "must not emit XML.nodeType: " + out);
        }
    }

    @Test
    public void newFields32IgnoredIn31Deserialization() throws Exception {
        String doc = "openapi: 3.1.0\n" +
                "info:\n  title: t\n  version: '1'\n" +
                "servers:\n  - url: https://api.example.com\n    name: production\n" +
                "tags:\n  - name: pets\n    summary: s\n    parent: animals\n    kind: nav\n" +
                "paths: {}\n" +
                "components:\n" +
                "  securitySchemes:\n" +
                "    oauth:\n" +
                "      type: oauth2\n" +
                "      deprecated: true\n" +
                "      oauth2MetadataUrl: https://auth.example.com/meta\n" +
                "      flows:\n" +
                "        deviceAuthorization:\n" +
                "          deviceAuthorizationUrl: https://auth.example.com/device\n" +
                "          tokenUrl: https://auth.example.com/token\n" +
                "        authorizationCode:\n" +
                "          authorizationUrl: https://auth.example.com/authorize\n" +
                "          tokenUrl: https://auth.example.com/token\n" +
                "          deviceAuthorizationUrl: https://auth.example.com/device\n";
        OpenAPI readBack = Yaml31.mapper().readValue(doc, OpenAPI.class);
        assertNull(readBack.getServers().get(0).getName(), "3.1 mapper must not bind Server.name");
        assertNull(readBack.getTags().get(0).getSummary(), "3.1 mapper must not bind Tag.summary");
        SecurityScheme scheme = readBack.getComponents().getSecuritySchemes().get("oauth");
        assertNull(scheme.getDeprecated(), "3.1 mapper must not bind SecurityScheme.deprecated");
        assertNull(scheme.getOauth2MetadataUrl(), "3.1 mapper must not bind SecurityScheme.oauth2MetadataUrl");
        assertNull(scheme.getFlows().getDeviceAuthorization(), "3.1 mapper must not bind flows.deviceAuthorization");
        assertNotNull(scheme.getFlows().getAuthorizationCode(), "authorizationCode itself must bind in 3.1");
        assertNull(scheme.getFlows().getAuthorizationCode().getDeviceAuthorizationUrl(),
                "3.1 mapper must not bind OAuthFlow.deviceAuthorizationUrl");
    }

    @Test
    public void exampleNullDataValueSerializes() throws Exception {
        // dataValue of type 'any' may legitimately be null: the set-flag mirrors valueSetFlag
        OpenAPI doc = buildDoc();
        doc.setComponents(new Components().addExamples("nullEx", new Example().dataValue(null)));
        String out = Json32.mapper().writeValueAsString(doc);
        assertTrue(out.contains("\"dataValue\":null"), "explicit null dataValue must serialize: " + out);

        // the explicit-null write path must not leak the field to earlier versions either
        String out30 = Json.mapper().writeValueAsString(doc);
        assertFalse(out30.contains("dataValue"), "3.0 must not emit dataValue even when set-flag is on: " + out30);
        String out31 = Json31.mapper().writeValueAsString(doc);
        assertFalse(out31.contains("dataValue"), "3.1 must not emit dataValue even when set-flag is on: " + out31);
    }

    @Test
    public void schemaToStringIncludes31KeywordsForV32() {
        // Regression for Schema.java toString fix: the 3.1-style keyword block must be
        // emitted for V32 too (was `== V31`, now `!= V30`).
        Schema schema = new Schema();
        schema.setSpecVersion(SpecVersion.V32);
        schema.setPatternProperties(java.util.Collections.singletonMap("^a", new Schema()));
        String out = schema.toString();
        assertTrue(out.contains("patternProperties:"), "V32 Schema.toString must print 3.1 keywords: " + out);

        schema.setSpecVersion(SpecVersion.V30);
        assertFalse(schema.toString().contains("patternProperties:"), "V30 must not print 3.1 keywords");
    }

    @Test
    public void selfSerializesIn32() throws Exception {
        OpenAPI doc = buildDoc().$self("https://example.com/openapi.yaml");
        String out = Json32.mapper().writeValueAsString(doc);
        assertTrue(out.contains("\"$self\":\"https://example.com/openapi.yaml\""),
                "3.2 JSON output should contain '$self': " + out);
        String yaml = Yaml32.mapper().writeValueAsString(doc);
        assertTrue(yaml.contains("$self: \"https://example.com/openapi.yaml\"") ||
                        yaml.contains("$self: https://example.com/openapi.yaml"),
                "3.2 YAML output should contain '$self': " + yaml);
    }

    @Test
    public void selfHiddenIn30And31() throws Exception {
        OpenAPI doc = buildDoc().$self("https://example.com/openapi.yaml");
        String out30 = Json.mapper().writeValueAsString(doc);
        assertFalse(out30.contains("$self"), "3.0 output must not contain '$self': " + out30);
        String out31 = Json31.mapper().writeValueAsString(doc);
        assertFalse(out31.contains("$self"), "3.1 output must not contain '$self': " + out31);
        String out30Yaml = io.swagger.v3.core.util.Yaml.mapper().writeValueAsString(doc);
        assertFalse(out30Yaml.contains("$self"), "3.0 YAML output must not contain '$self': " + out30Yaml);
    }

    @Test
    public void selfRoundTrip32() throws Exception {
        OpenAPI doc = buildDoc().$self("https://example.com/openapi.yaml");
        String out = Json32.mapper().writeValueAsString(doc);
        OpenAPI readBack = Json32.mapper().readValue(out, OpenAPI.class);
        assertEquals(readBack.get$self(), "https://example.com/openapi.yaml");

        String yaml = Yaml32.mapper().writeValueAsString(doc);
        OpenAPI readBackYaml = Yaml32.mapper().readValue(yaml, OpenAPI.class);
        assertEquals(readBackYaml.get$self(), "https://example.com/openapi.yaml");
    }

    @Test
    public void selfIgnoredIn30And31Deserialization() throws Exception {
        String doc = "openapi: 3.1.0\n" +
                "$self: https://example.com/openapi.yaml\n" +
                "info:\n" +
                "  title: t\n" +
                "  version: '1'\n" +
                "paths: {}\n";
        OpenAPI read31 = Yaml31.mapper().readValue(doc, OpenAPI.class);
        assertNull(read31.get$self(), "3.1 mapper must not bind $self");

        String doc30 = doc.replace("3.1.0", "3.0.3");
        OpenAPI read30 = io.swagger.v3.core.util.Yaml.mapper().readValue(doc30, OpenAPI.class);
        assertNull(read30.get$self(), "3.0 mapper must not bind $self");
    }

    @Test
    public void selfHiddenInConverterMapper() throws Exception {
        // the V30-style converter mapper shares OpenAPIMixin and must not emit $self either
        OpenAPI doc = buildDoc().$self("https://example.com/openapi.yaml");
        String out = io.swagger.v3.core.util.ObjectMapperFactory.createJsonConverter()
                .writeValueAsString(doc);
        assertFalse(out.contains("$self"), "converter mapper must not emit '$self': " + out);
    }

    @Test
    public void selfSurvivesSpecFilter() {
        // SpecFilter copies root fields onto a fresh OpenAPI; $self must be carried over
        OpenAPI doc = buildDoc().$self("https://example.com/openapi.yaml");
        OpenAPI filtered = new SpecFilter().filter(doc, new NoOpOperationsFilter(), null, null, null);
        assertEquals(filtered.get$self(), "https://example.com/openapi.yaml",
                "SpecFilter must preserve $self");
    }

    @Test
    public void querystringParameterBinds32() throws Exception {
        String doc = "openapi: 3.2.0\n" +
                "info:\n" +
                "  title: t\n" +
                "  version: '1'\n" +
                "paths:\n" +
                "  /pets:\n" +
                "    get:\n" +
                "      parameters:\n" +
                "        - name: queryString\n" +
                "          in: querystring\n" +
                "          content:\n" +
                "            application/x-www-form-urlencoded:\n" +
                "              schema:\n" +
                "                type: object\n" +
                "      responses:\n" +
                "        '200':\n" +
                "          description: ok\n";
        OpenAPI readBack = Yaml32.mapper().readValue(doc, OpenAPI.class);
        Parameter p = readBack.getPaths().get("/pets").getGet().getParameters().get(0);
        assertTrue(p instanceof io.swagger.v3.oas.models.parameters.QueryStringParameter,
                "in: querystring must bind to QueryStringParameter, got " + p.getClass());
        assertEquals(p.getIn(), "querystring");
        assertEquals(p.getName(), "queryString");
        assertNotNull(p.getContent(), "querystring parameter must keep its content map");
        MediaType mt = p.getContent().get("application/x-www-form-urlencoded");
        assertNotNull(mt);
        assertNotNull(mt.getSchema(), "media type schema must bind");
        assertTrue(mt.getSchema().getTypes().contains("object"),
                "schema types must contain 'object': " + mt.getSchema().getTypes());
    }

    @Test
    public void querystringParameterRoundTrip32() throws Exception {
        Parameter qs = new io.swagger.v3.oas.models.parameters.QueryStringParameter()
                .name("queryString")
                .content(new Content().addMediaType("application/x-www-form-urlencoded",
                        new MediaType().schema(new Schema())));
        String out = Json32.mapper().writeValueAsString(qs);
        assertTrue(out.contains("\"in\":\"querystring\""), "must serialize in: querystring: " + out);
        Parameter readBack = Json32.mapper().readValue(out, Parameter.class);
        assertTrue(readBack instanceof io.swagger.v3.oas.models.parameters.QueryStringParameter);
        assertEquals(readBack.getIn(), "querystring");
        assertEquals(readBack.getName(), "queryString");
        assertNotNull(readBack.getContent(), "round-trip must keep content");
        assertNotNull(readBack.getContent().get("application/x-www-form-urlencoded").getSchema());
    }

    @Test
    public void querystringNotBoundIn30And31() throws Exception {
        String json = "{\"name\":\"queryString\",\"in\":\"querystring\"," +
                "\"content\":{\"application/x-www-form-urlencoded\":{\"schema\":{\"type\":\"object\"}}}}";
        assertNull(Yaml31.mapper().readValue(
                "name: queryString\nin: querystring\ncontent:\n  application/x-www-form-urlencoded:\n    schema:\n      type: object\n",
                Parameter.class), "3.1 must not bind in: querystring");
        assertNull(Json.mapper().readValue(json, Parameter.class),
                "3.0 must not bind in: querystring");
    }

    @Test
    public void cookieStyleRoundTrip32() throws Exception {
        Parameter cookie = new io.swagger.v3.oas.models.parameters.CookieParameter()
                .name("sessionId")
                .style(Parameter.StyleEnum.COOKIE);
        String out = Json32.mapper().writeValueAsString(cookie);
        assertTrue(out.contains("\"style\":\"cookie\""), "must serialize style: cookie: " + out);
        Parameter readBack = Json32.mapper().readValue(out, Parameter.class);
        assertEquals(readBack.getStyle(), Parameter.StyleEnum.COOKIE);
    }

    @Test
    public void querystringAnnotationEnum() {
        assertEquals(io.swagger.v3.oas.annotations.enums.ParameterIn.QUERYSTRING.toString(),
                "querystring");
    }

    @Test
    public void cookieStyleRejectedIn30And31() {
        // style: cookie is a 3.2 addition; earlier versions must fail as they did
        // when 'cookie' was an unknown enum value
        String json = "{\"name\":\"sessionId\",\"in\":\"cookie\",\"style\":\"cookie\"}";
        try {
            Json.mapper().readValue(json, Parameter.class);
            org.testng.Assert.fail("3.0 must reject style: cookie");
        } catch (Exception expected) {
            assertTrue(expected.getMessage().contains("cookie"), expected.getMessage());
        }
        try {
            Json31.mapper().readValue(json, Parameter.class);
            org.testng.Assert.fail("3.1 must reject style: cookie");
        } catch (Exception expected) {
            assertTrue(expected.getMessage().contains("cookie"), expected.getMessage());
        }
    }
}
