package io.swagger.v3.core.serialization;

import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.core.filter.resources.NoOpOperationsFilter;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Json32;
import io.swagger.v3.core.util.Yaml;
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
import io.swagger.v3.oas.models.media.Encoding;
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
import java.util.List;
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

    private MediaType buildMultipartMediaType() {
        Encoding inner = new Encoding().contentType("text/plain");
        Encoding nested = new Encoding()
                .contentType("application/json")
                .addEncoding("prop", new Encoding().contentType("text/plain"))
                .addPrefixEncoding(new Encoding().contentType("image/png"))
                .itemEncoding(new Encoding().contentType("text/csv"));
        return new MediaType()
                .schema(new Schema())
                .addPrefixEncoding(inner)
                .itemEncoding(new Encoding().contentType("application/octet-stream"))
                .addEncoding("named", nested);
    }

    @Test
    public void positionalEncodingSerializes32() throws Exception {
        MediaType mt = buildMultipartMediaType();
        String out = Json32.mapper().writeValueAsString(mt);
        assertTrue(out.contains("\"prefixEncoding\":"), "3.2 must emit prefixEncoding: " + out);
        assertTrue(out.contains("\"itemEncoding\":"), "3.2 must emit itemEncoding: " + out);
        // pin the nested Encoding.encoding property name at /encoding/named/encoding/prop
        com.fasterxml.jackson.databind.JsonNode tree = Json32.mapper().readTree(out);
        assertEquals(tree.at("/encoding/named/encoding/prop/contentType").asText(), "text/plain",
                "nested Encoding.encoding must serialize as 'encoding': " + out);
        assertEquals(tree.at("/encoding/named/prefixEncoding/0/contentType").asText(), "image/png",
                "nested Encoding.prefixEncoding must serialize: " + out);
        assertEquals(tree.at("/encoding/named/itemEncoding/contentType").asText(), "text/csv",
                "nested Encoding.itemEncoding must serialize: " + out);
        String yaml = Yaml32.mapper().writeValueAsString(mt);
        assertTrue(yaml.contains("prefixEncoding:"), "3.2 YAML must emit prefixEncoding: " + yaml);
        assertTrue(yaml.contains("itemEncoding:"), "3.2 YAML must emit itemEncoding: " + yaml);
    }

    @Test
    public void positionalEncodingRoundTrip32() throws Exception {
        MediaType mt = buildMultipartMediaType();
        String out = Json32.mapper().writeValueAsString(mt);
        MediaType readBack = Json32.mapper().readValue(out, MediaType.class);
        assertNotNull(readBack.getPrefixEncoding());
        assertEquals(readBack.getPrefixEncoding().size(), 1);
        assertEquals(readBack.getPrefixEncoding().get(0).getContentType(), "text/plain");
        assertNotNull(readBack.getItemEncoding());
        assertEquals(readBack.getItemEncoding().getContentType(), "application/octet-stream");
        Encoding named = readBack.getEncoding().get("named");
        assertNotNull(named.getEncoding(), "nested Encoding.encoding map must bind");
        assertEquals(named.getEncoding().get("prop").getContentType(), "text/plain");
        assertEquals(named.getPrefixEncoding().get(0).getContentType(), "image/png",
                "nested Encoding.prefixEncoding must bind");
        assertEquals(named.getItemEncoding().getContentType(), "text/csv",
                "nested Encoding.itemEncoding must bind");
    }

    @Test
    public void positionalEncodingBindsFromDocument32() throws Exception {
        // fixed input pins the wire property names, independent of the serializer
        String doc = "encoding:\n" +
                "  named:\n" +
                "    contentType: application/json\n" +
                "    encoding:\n" +
                "      prop:\n" +
                "        contentType: text/plain\n" +
                "    prefixEncoding:\n" +
                "      - contentType: image/png\n" +
                "    itemEncoding:\n" +
                "      contentType: text/csv\n";
        MediaType readBack = Yaml32.mapper().readValue(doc, MediaType.class);
        Encoding named = readBack.getEncoding().get("named");
        assertNotNull(named);
        assertEquals(named.getEncoding().get("prop").getContentType(), "text/plain");
        assertEquals(named.getPrefixEncoding().get(0).getContentType(), "image/png");
        assertEquals(named.getItemEncoding().getContentType(), "text/csv");
    }

    @Test
    public void positionalEncodingHiddenIn30And31() throws Exception {
        MediaType mt = buildMultipartMediaType();
        String out30 = Json.mapper().writeValueAsString(mt);
        assertFalse(out30.contains("prefixEncoding"), "3.0 must not emit prefixEncoding: " + out30);
        assertFalse(out30.contains("itemEncoding"), "3.0 must not emit itemEncoding: " + out30);
        // nested Encoding.encoding is a 3.2 field too, so a 3.0 write drops it
        assertFalse(out30.contains("\"prop\""), "3.0 must not emit nested encoding: " + out30);
        // the pre-existing MediaType.encoding map itself must still serialize
        assertTrue(out30.contains("\"named\""), "3.0 must keep MediaType.encoding: " + out30);
        assertTrue(out30.contains("\"contentType\":\"application/json\""),
                "3.0 must keep non-3.2 Encoding fields: " + out30);
        String out31 = Json31.mapper().writeValueAsString(mt);
        assertFalse(out31.contains("prefixEncoding"), "3.1 must not emit prefixEncoding: " + out31);
        assertFalse(out31.contains("itemEncoding"), "3.1 must not emit itemEncoding: " + out31);
        assertFalse(out31.contains("\"prop\""), "3.1 must not emit nested encoding: " + out31);
        assertTrue(out31.contains("\"named\""), "3.1 must keep MediaType.encoding: " + out31);
    }

    @Test
    public void positionalEncodingNotBoundIn31() throws Exception {
        String doc = "schema:\n" +
                "  type: object\n" +
                "prefixEncoding:\n" +
                "  - contentType: text/plain\n" +
                "itemEncoding:\n" +
                "  contentType: application/octet-stream\n" +
                "encoding:\n" +
                "  named:\n" +
                "    contentType: application/json\n" +
                "    encoding:\n" +
                "      prop:\n" +
                "        contentType: text/plain\n" +
                "    prefixEncoding:\n" +
                "      - contentType: image/png\n" +
                "    itemEncoding:\n" +
                "      contentType: text/csv\n";
        MediaType read31 = Yaml31.mapper().readValue(doc, MediaType.class);
        assertNull(read31.getPrefixEncoding(), "3.1 must not bind prefixEncoding");
        assertNull(read31.getItemEncoding(), "3.1 must not bind itemEncoding");
        Encoding named31 = read31.getEncoding().get("named");
        assertNotNull(named31, "3.1 must still bind the MediaType.encoding entry itself");
        assertEquals(named31.getContentType(), "application/json");
        assertNull(named31.getEncoding(), "3.1 must not bind nested Encoding.encoding");
        assertNull(named31.getPrefixEncoding(), "3.1 must not bind nested Encoding.prefixEncoding");
        assertNull(named31.getItemEncoding(), "3.1 must not bind nested Encoding.itemEncoding");
    }

    @Test
    public void positionalEncodingHiddenInConverter() throws Exception {
        MediaType mt = buildMultipartMediaType();
        String out = io.swagger.v3.core.util.ObjectMapperFactory.createJsonConverter()
                .writeValueAsString(mt);
        assertFalse(out.contains("prefixEncoding"), "converter must not emit prefixEncoding: " + out);
        assertFalse(out.contains("itemEncoding"), "converter must not emit itemEncoding: " + out);
        // the V30 encoding map itself stays, but the nested 3.2 fields must be gone
        com.fasterxml.jackson.databind.JsonNode tree =
                io.swagger.v3.core.util.ObjectMapperFactory.createJsonConverter().readTree(out);
        assertTrue(tree.at("/encoding/named").isContainerNode() || !tree.at("/encoding/named").isMissingNode(),
                "converter must keep the MediaType.encoding entry: " + out);
        assertTrue(tree.at("/encoding/named/encoding").isMissingNode(),
                "converter must drop nested Encoding.encoding: " + out);
    }

    private PathItem buildAdditionalOpsPathItem() {
        PathItem item = new PathItem().get(new Operation()
                .operationId("getPets")
                .responses(new ApiResponses().addApiResponse("200",
                        new ApiResponse().description("ok"))));
        item.addAdditionalOperation("PURGE", new Operation()
                .operationId("purgePets")
                .responses(new ApiResponses().addApiResponse("204",
                        new ApiResponse().description("purged"))));
        return item;
    }

    @Test
    public void additionalOperationsSerialize32() throws Exception {
        OpenAPI doc = buildDoc();
        doc.getPaths().put("/pets", buildAdditionalOpsPathItem());
        String out = Json32.mapper().writeValueAsString(doc);
        assertTrue(out.contains("\"additionalOperations\""), "3.2 must emit additionalOperations: " + out);
        // keys keep their original case per the spec (no lowercasing)
        assertTrue(out.contains("\"PURGE\""), "method key must keep original case: " + out);
        String yaml = Yaml32.mapper().writeValueAsString(doc);
        assertTrue(yaml.contains("additionalOperations:"), "3.2 YAML must emit it: " + yaml);
        assertTrue(yaml.contains("PURGE:"), "YAML key must keep original case: " + yaml);
    }

    @Test
    public void additionalOperationsRoundTrip32() throws Exception {
        OpenAPI doc = buildDoc();
        doc.getPaths().put("/pets", buildAdditionalOpsPathItem());
        String out = Json32.mapper().writeValueAsString(doc);
        OpenAPI readBack = Json32.mapper().readValue(out, OpenAPI.class);
        PathItem item = readBack.getPaths().get("/pets");
        assertNotNull(item.getAdditionalOperations());
        Operation purge = item.getAdditionalOperations().get("PURGE");
        assertNotNull(purge, "PURGE key must survive round-trip with original case");
        assertEquals(purge.getOperationId(), "purgePets");
    }

    @Test
    public void additionalOperationsHiddenIn30And31() throws Exception {
        OpenAPI doc = buildDoc();
        doc.getPaths().put("/pets", buildAdditionalOpsPathItem());
        String out30 = Json.mapper().writeValueAsString(doc);
        assertFalse(out30.contains("additionalOperations"), "3.0 must not emit it: " + out30);
        assertFalse(out30.contains("PURGE"), "3.0 must not leak the operation: " + out30);
        String out31 = Json31.mapper().writeValueAsString(doc);
        assertFalse(out31.contains("additionalOperations"), "3.1 must not emit it: " + out31);
        assertFalse(out31.contains("PURGE"), "3.1 must not leak the operation: " + out31);
    }

    @Test
    public void additionalOperationsNotBoundIn31() throws Exception {
        String doc = "get:\n" +
                "  operationId: getPets\n" +
                "  responses:\n" +
                "    '200':\n" +
                "      description: ok\n" +
                "additionalOperations:\n" +
                "  PURGE:\n" +
                "    operationId: purgePets\n" +
                "    responses:\n" +
                "      '204':\n" +
                "        description: purged\n";
        PathItem read31 = Yaml31.mapper().readValue(doc, PathItem.class);
        assertNull(read31.getAdditionalOperations(), "3.1 must not bind additionalOperations");
        PathItem read30 = io.swagger.v3.core.util.Yaml.mapper().readValue(doc, PathItem.class);
        assertNull(read30.getAdditionalOperations(), "3.0 must not bind additionalOperations");
    }

    @Test
    public void additionalOperationsInReadOperations() {
        PathItem item = buildAdditionalOpsPathItem();
        List<Operation> ops = item.readOperations();
        assertEquals(ops.size(), 2, "readOperations must include additionalOperations values");
        assertEquals(ops.get(1).getOperationId(), "purgePets");
        // enum-keyed map stays limited to fixed methods
        assertFalse(item.readOperationsMap().values().stream()
                .anyMatch(o -> "purgePets".equals(o.getOperationId())),
                "readOperationsMap cannot express non-enum methods");
    }

    @Test
    public void additionalOperationsInWebhooks32() throws Exception {
        // webhooks reuse PathItem; additionalOperations must work there too
        String doc = "openapi: 3.2.0\n" +
                "info:\n" +
                "  title: t\n" +
                "  version: '1'\n" +
                "webhooks:\n" +
                "  hook:\n" +
                "    additionalOperations:\n" +
                "      NOTIFY:\n" +
                "        operationId: notifyHook\n" +
                "        responses:\n" +
                "          '200':\n" +
                "            description: ok\n" +
                "paths: {}\n";
        OpenAPI readBack = Yaml32.mapper().readValue(doc, OpenAPI.class);
        PathItem hook = readBack.getWebhooks().get("hook");
        assertNotNull(hook.getAdditionalOperations());
        assertEquals(hook.getAdditionalOperations().get("NOTIFY").getOperationId(), "notifyHook");
    }

    @Test
    public void additionalOperationsDuplicateFixedMethodKeptLenient32() throws Exception {
        // spec forbids keys duplicating fixed methods; the lenient model keeps the
        // data and leaves enforcement to parser-side validation
        String doc = "get:\n" +
                "  operationId: getPets\n" +
                "  responses:\n" +
                "    '200':\n" +
                "      description: ok\n" +
                "additionalOperations:\n" +
                "  get:\n" +
                "    operationId: dupGet\n" +
                "    responses:\n" +
                "      '200':\n" +
                "        description: dup\n";
        PathItem readBack = Yaml32.mapper().readValue(doc, PathItem.class);
        assertNotNull(readBack.getAdditionalOperations().get("get"),
                "duplicate fixed-method key is retained (lenient); validation is parser-side");
        assertEquals(readBack.getGet().getOperationId(), "getPets",
                "fixed field wins for the fixed key");
    }

    @Test
    public void additionalOperationsSurviveSpecFilter() {
        OpenAPI doc = buildDoc();
        doc.getPaths().put("/pets", buildAdditionalOpsPathItem());
        OpenAPI filtered = new SpecFilter().filter(doc, new NoOpOperationsFilter(), null, null, null);
        PathItem item = filtered.getPaths().get("/pets");
        assertNotNull(item.getAdditionalOperations(), "SpecFilter must keep additionalOperations");
        assertEquals(item.getAdditionalOperations().get("PURGE").getOperationId(), "purgePets");
    }

    @Test
    public void additionalOperationsOnlyPathItemSurvivesSpecFilter() {
        // a PathItem whose only operations are additional must not be dropped as empty
        OpenAPI doc = buildDoc();
        PathItem onlyAdditional = new PathItem();
        onlyAdditional.addAdditionalOperation("PURGE", new Operation()
                .operationId("purgePets")
                .responses(new ApiResponses().addApiResponse("204",
                        new ApiResponse().description("purged"))));
        doc.getPaths().put("/purge", onlyAdditional);
        OpenAPI filtered = new SpecFilter().filter(doc, new NoOpOperationsFilter(), null, null, null);
        assertNotNull(filtered.getPaths().get("/purge"),
                "path item with only additionalOperations must survive filtering");
        assertNotNull(filtered.getPaths().get("/purge").getAdditionalOperations().get("PURGE"));
    }

    @Test
    public void additionalOperationsSchemaRefsSurviveUnreferencedPruning() {
        // a schema referenced only from an additional operation must not be pruned
        OpenAPI doc = buildDoc();
        Schema pet = new Schema().typesItem("object");
        doc.setComponents(new Components().addSchemas("Pet", pet));

        PathItem item = new PathItem();
        Operation purge = new Operation()
                .operationId("purgePets")
                .responses(new ApiResponses().addApiResponse("200",
                        new ApiResponse().description("ok")
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema()
                                                .$ref("#/components/schemas/Pet"))))));
        item.addAdditionalOperation("PURGE", purge);
        doc.getPaths().put("/pets", item);

        OpenAPI filtered = new SpecFilter().filter(doc, new AbstractSpecFilter() {
            @Override
            public boolean isRemovingUnreferencedDefinitions() {
                return true;
            }
        }, null, null, null);
        assertNotNull(filtered.getPaths().get("/pets").getAdditionalOperations().get("PURGE"),
                "additional operation must survive");
        assertNotNull(filtered.getComponents().getSchemas().get("Pet"),
                "schema referenced only by an additional operation must not be pruned");
    }

    // OpenAPI 3.2: content maps accept Reference Object values, and
    // Components gains a reusable 'mediaTypes' map

    @Test
    public void contentMapRefValueRoundTrip32() throws Exception {
        String doc = "openapi: 3.2.0\n" +
                "info:\n" +
                "  title: t\n" +
                "  version: '1'\n" +
                "paths:\n" +
                "  /pets:\n" +
                "    get:\n" +
                "      operationId: getPets\n" +
                "      responses:\n" +
                "        '200':\n" +
                "          description: ok\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              $ref: '#/components/mediaTypes/Pet'\n" +
                "components:\n" +
                "  mediaTypes:\n" +
                "    Pet:\n" +
                "      schema:\n" +
                "        type: object\n";
        OpenAPI readBack = Yaml32.mapper().readValue(doc, OpenAPI.class);
        MediaType mediaType = readBack.getPaths().get("/pets").getGet()
                .getResponses().get("200").getContent().get("application/json");
        assertNotNull(mediaType, "content map value must bind");
        assertEquals(mediaType.get$ref(), "#/components/mediaTypes/Pet",
                "Reference Object value must bind to MediaType.$ref");
        assertNull(mediaType.getSchema());

        String serialized = Yaml32.mapper().writeValueAsString(readBack);
        assertTrue(serialized.contains("$ref: '#/components/mediaTypes/Pet'")
                        || serialized.contains("$ref: \"#/components/mediaTypes/Pet\""),
                "3.2 serialization must keep the content map $ref value");
        assertTrue(serialized.contains("mediaTypes:"),
                "3.2 serialization must keep components.mediaTypes");

        // JSON round-trip too
        OpenAPI jsonReadBack = Json32.mapper().readValue(
                Json32.mapper().writeValueAsString(readBack), OpenAPI.class);
        assertEquals(jsonReadBack.getPaths().get("/pets").getGet()
                        .getResponses().get("200").getContent()
                        .get("application/json").get$ref(),
                "#/components/mediaTypes/Pet");
        assertNotNull(jsonReadBack.getComponents().getMediaTypes().get("Pet"));
    }

    @Test
    public void contentMapRefValueNotBound3031() throws Exception {
        String doc = "content:\n" +
                "  application/json:\n" +
                "    $ref: '#/components/mediaTypes/Pet'\n";
        ApiResponse res32 = Yaml32.mapper().readValue(
                "description: ok\n" + doc, ApiResponse.class);
        assertEquals(res32.getContent().get("application/json").get$ref(),
                "#/components/mediaTypes/Pet", "3.2 binds content $ref");

        ApiResponse res31 = Yaml31.mapper().readValue(
                "description: ok\n" + doc, ApiResponse.class);
        assertNull(res31.getContent().get("application/json").get$ref(),
                "3.1 must not bind content $ref");
        ApiResponse res30 = io.swagger.v3.core.util.Yaml.mapper().readValue(
                "description: ok\n" + doc, ApiResponse.class);
        assertNull(res30.getContent().get("application/json").get$ref(),
                "3.0 must not bind content $ref");
    }

    @Test
    public void contentMapRefValueNotSerialized3031() throws Exception {
        ApiResponse res = new ApiResponse().description("ok")
                .content(new Content().addMediaType("application/json",
                        new MediaType().$ref("#/components/mediaTypes/Pet")));
        assertFalse(io.swagger.v3.core.util.Yaml.mapper().writeValueAsString(res).contains("$ref"),
                "3.0 must not emit MediaType.$ref");
        assertFalse(Yaml31.mapper().writeValueAsString(res).contains("$ref"),
                "3.1 must not emit MediaType.$ref");
        assertFalse(Json.mapper().writeValueAsString(res).contains("$ref"),
                "3.0 JSON must not emit MediaType.$ref");
    }

    @Test
    public void componentsMediaTypesRoundTrip32() throws Exception {
        String doc = "openapi: 3.2.0\n" +
                "info:\n" +
                "  title: t\n" +
                "  version: '1'\n" +
                "paths: {}\n" +
                "components:\n" +
                "  mediaTypes:\n" +
                "    Pet:\n" +
                "      schema:\n" +
                "        type: object\n" +
                "    AliasedPet:\n" +
                "      $ref: '#/components/mediaTypes/Pet'\n";
        OpenAPI readBack = Yaml32.mapper().readValue(doc, OpenAPI.class);
        Map<String, MediaType> mediaTypes = readBack.getComponents().getMediaTypes();
        assertNotNull(mediaTypes, "3.2 must bind components.mediaTypes");
        assertEquals(mediaTypes.get("AliasedPet").get$ref(), "#/components/mediaTypes/Pet",
                "mediaTypes entries may themselves be Reference Objects");
        assertNotNull(mediaTypes.get("Pet").getSchema());

        String serialized = Yaml32.mapper().writeValueAsString(readBack);
        assertTrue(serialized.contains("mediaTypes:"));
        OpenAPI roundTripped = Yaml32.mapper().readValue(serialized, OpenAPI.class);
        assertEquals(roundTripped.getComponents().getMediaTypes().get("AliasedPet").get$ref(),
                "#/components/mediaTypes/Pet");
    }

    @Test
    public void componentsMediaTypesNotSerializedOrBound3031() throws Exception {
        OpenAPI doc = buildDoc()
                .components(new Components().addMediaType("Pet",
                        new MediaType().schema(new Schema().typesItem("object"))));
        assertFalse(Yaml31.mapper().writeValueAsString(doc).contains("mediaTypes"),
                "3.1 must not emit components.mediaTypes");
        assertFalse(io.swagger.v3.core.util.Yaml.mapper().writeValueAsString(doc).contains("mediaTypes"),
                "3.0 must not emit components.mediaTypes");
        assertFalse(Json.mapper().writeValueAsString(doc).contains("mediaTypes"),
                "3.0 JSON must not emit components.mediaTypes");
        // legacy converter mapper is a 3.0-shape mapper
        assertFalse(io.swagger.v3.core.util.ObjectMapperFactory.createJsonConverter()
                        .writeValueAsString(doc).contains("mediaTypes"),
                "converter mapper must not emit components.mediaTypes");

        String yaml = "components:\n  mediaTypes:\n    Pet:\n      schema:\n        type: object\n";
        assertNull(Yaml31.mapper().readValue(
                        "openapi: 3.1.0\ninfo:\n  title: t\n  version: '1'\npaths: {}\n" + yaml,
                        OpenAPI.class).getComponents().getMediaTypes(),
                "3.1 must not bind components.mediaTypes");
    }

    @Test
    public void mediaTypesSurviveSpecFilter() {
        OpenAPI doc = buildDoc();
        doc.setComponents(new Components().addMediaType("Pet",
                new MediaType().$ref("#/components/mediaTypes/Base")));
        OpenAPI filtered = new SpecFilter().filter(doc, new NoOpOperationsFilter(), null, null, null);
        assertNotNull(filtered.getComponents().getMediaTypes(),
                "SpecFilter must keep components.mediaTypes");
        assertEquals(filtered.getComponents().getMediaTypes().get("Pet").get$ref(),
                "#/components/mediaTypes/Base");
    }

    @Test
    public void mediaTypesSchemaRefsSurviveUnreferencedPruning() {
        // a schema referenced only from a components.mediaTypes entry must not be pruned
        OpenAPI doc = buildDoc();
        doc.setComponents(new Components()
                .addSchemas("PetSchema", new Schema().typesItem("object"))
                .addMediaType("Pet", new MediaType()
                        .schema(new Schema().$ref("#/components/schemas/PetSchema"))));
        OpenAPI filtered = new SpecFilter().filter(doc, new AbstractSpecFilter() {
            @Override
            public boolean isRemovingUnreferencedDefinitions() {
                return true;
            }
        }, null, null, null);
        assertNotNull(filtered.getComponents().getSchemas().get("PetSchema"),
                "schema referenced only by components.mediaTypes must not be pruned");
    }

    @Test
    public void contentMapRefValueSuppressesSiblings32() throws Exception {
        // a $ref content value is a Reference Object: sibling fields are ignored
        ApiResponse res = new ApiResponse().description("ok")
                .content(new Content().addMediaType("application/json",
                        new MediaType()
                                .$ref("#/components/mediaTypes/Pet")
                                .schema(new Schema().typesItem("object"))));
        String serialized = Yaml32.mapper().writeValueAsString(res);
        assertTrue(serialized.contains("$ref"), "3.2 must emit the $ref value");
        assertFalse(serialized.contains("schema:"),
                "3.2 must suppress sibling fields of a Reference Object value");
        // same lenient input under 3.1: $ref is hidden, schema is emitted
        String serialized31 = Yaml31.mapper().writeValueAsString(res);
        assertTrue(serialized31.contains("schema:"));
        assertFalse(serialized31.contains("$ref"));
    }

    @Test
    public void mediaTypesRefDoesNotRetainSameNamedSchema() {
        // '#/components/mediaTypes/X' must not keep an unrelated schemas.X alive
        OpenAPI doc = buildDoc();
        doc.setComponents(new Components()
                .addSchemas("Base", new Schema().typesItem("object"))
                .addMediaType("Pet", new MediaType().$ref("#/components/mediaTypes/Base")));
        OpenAPI filtered = new SpecFilter().filter(doc, new AbstractSpecFilter() {
            @Override
            public boolean isRemovingUnreferencedDefinitions() {
                return true;
            }
        }, null, null, null);
        assertNull(filtered.getComponents().getSchemas() == null
                        ? null
                        : filtered.getComponents().getSchemas().get("Base"),
                "schema named like a mediaTypes ref target must still be pruned");
        assertNotNull(filtered.getComponents().getMediaTypes().get("Pet"));
    }

    @Test
    public void contentMapRefToSchemaRetainsSchema() {
        // lenient case: a content $ref pointing at a schema keeps it alive
        OpenAPI doc = buildDoc();
        doc.setComponents(new Components()
                .addSchemas("Pet", new Schema().typesItem("object")));
        doc.getPaths().get("/pets").getQuery().getResponses().get("200")
                .content(new Content().addMediaType("application/json",
                        new MediaType().$ref("#/components/schemas/Pet")));
        OpenAPI filtered = new SpecFilter().filter(doc, new AbstractSpecFilter() {
            @Override
            public boolean isRemovingUnreferencedDefinitions() {
                return true;
            }
        }, null, null, null);
        assertNotNull(filtered.getComponents().getSchemas().get("Pet"),
                "schema referenced by a content $ref must not be pruned");
    }

    @Test
    public void mediaTypesEncodingSchemaRefsSurviveUnreferencedPruning() {
        // a schema referenced inside a mediaTypes entry's encoding headers must not be pruned
        OpenAPI doc = buildDoc();
        Encoding encoding = new Encoding();
        encoding.addHeader("X-Meta", new io.swagger.v3.oas.models.headers.Header()
                .schema(new Schema().$ref("#/components/schemas/MetaSchema")));
        doc.setComponents(new Components()
                .addSchemas("MetaSchema", new Schema().typesItem("object"))
                .addMediaType("Upload", new MediaType()
                        .schema(new Schema().typesItem("string"))
                        .addEncoding("part", encoding)));
        OpenAPI filtered = new SpecFilter().filter(doc, new AbstractSpecFilter() {
            @Override
            public boolean isRemovingUnreferencedDefinitions() {
                return true;
            }
        }, null, null, null);
        assertNotNull(filtered.getComponents().getSchemas().get("MetaSchema"),
                "schema referenced inside mediaTypes encoding headers must not be pruned");
    }

    @Test
    public void cookieStyleSerializesAndRoundTrips32() throws Exception {
        Parameter p = new io.swagger.v3.oas.models.parameters.CookieParameter().name("session");
        p.setStyle(Parameter.StyleEnum.COOKIE);
        String serialized = Json32.mapper().writeValueAsString(p);
        assertTrue(serialized.contains("\"style\":\"cookie\""));
        Parameter back = Json32.mapper().readValue(serialized, Parameter.class);
        assertEquals(back.getStyle(), Parameter.StyleEnum.COOKIE);
        assertEquals(back.getIn(), "cookie");
    }

    @Test
    public void cookieStyleRejectedBy30And31Serializers() {
        Parameter p = new io.swagger.v3.oas.models.parameters.CookieParameter().name("session");
        p.setStyle(Parameter.StyleEnum.COOKIE);
        // serializing a 3.2-only style must fail like deserializing it would, instead
        // of emitting a document the same mapper could not read back
        org.testng.Assert.expectThrows(com.fasterxml.jackson.databind.JsonMappingException.class,
                () -> Json.mapper().writeValueAsString(p));
        org.testng.Assert.expectThrows(com.fasterxml.jackson.databind.JsonMappingException.class,
                () -> Json31.mapper().writeValueAsString(p));
        org.testng.Assert.expectThrows(com.fasterxml.jackson.databind.JsonMappingException.class,
                () -> Yaml.mapper().writeValueAsString(p));
    }

    @Test
    public void specFilterClonePreserves32SchemaFields() {
        // bug: schema cloning used a pre-3.2 mapper and dropped discriminator.defaultMapping/xml.nodeType
        OpenAPI doc = buildDoc();
        Schema pet = new Schema().typesItem("object");
        pet.setDiscriminator(new Discriminator().propertyName("kind")
                .defaultMapping("#/components/schemas/Cat"));
        pet.setXml(new XML().name("pet").nodeType("attribute"));
        doc.setComponents(new Components().addSchemas("Pet", pet));
        OpenAPI filtered = new SpecFilter().filter(doc, new NoOpOperationsFilter(), null, null, null);
        Schema filteredPet = filtered.getComponents().getSchemas().get("Pet");
        assertNotNull(filteredPet);
        assertNotNull(filteredPet.getDiscriminator(), "discriminator must survive SpecFilter cloning");
        assertEquals(filteredPet.getDiscriminator().getDefaultMapping(), "#/components/schemas/Cat");
        assertNotNull(filteredPet.getXml(), "xml must survive SpecFilter cloning");
        assertEquals(filteredPet.getXml().getNodeType(), "attribute");
    }

    @Test
    public void defaultMappingRefSurvivesUnreferencedPruning() {
        // bug: a schema referenced only via discriminator.defaultMapping was pruned
        OpenAPI doc = buildDoc();
        Schema pet = new Schema().typesItem("object");
        pet.setDiscriminator(new Discriminator().propertyName("kind")
                .defaultMapping("#/components/schemas/Cat"));
        doc.setComponents(new Components()
                .addSchemas("Pet", pet)
                .addSchemas("Cat", new Schema().typesItem("object")));
        // reference Pet from a response so the discriminator owner itself survives
        Content content = new Content().addMediaType("application/json",
                new MediaType().schema(new Schema().$ref("#/components/schemas/Pet")));
        doc.getPaths().get("/pets").getQuery().getResponses().get("200").content(content);
        OpenAPI filtered = new SpecFilter().filter(doc, new AbstractSpecFilter() {
            @Override
            public boolean isRemovingUnreferencedDefinitions() {
                return true;
            }
        }, null, null, null);
        assertNotNull(filtered.getComponents().getSchemas().get("Pet"));
        assertNotNull(filtered.getComponents().getSchemas().get("Cat"),
                "schema referenced only via discriminator.defaultMapping must not be pruned");
    }

    @Test
    public void mediaTypeRefSchemaStillCollected31() {
        // regression guard: a MediaType carrying $ref must not mask the schema refs it
        // also holds; on pre-3.2 docs the schema traversal must still run
        OpenAPI doc = new OpenAPI()
                .openapi("3.1.0")
                .info(new Info().title("t").version("1"))
                .paths(new Paths());
        MediaType mediaType = new MediaType()
                .$ref("#/components/schemas/Unused")
                .schema(new Schema().$ref("#/components/schemas/Pet"));
        ApiResponse ok = new ApiResponse().description("ok")
                .content(new Content().addMediaType("application/json", mediaType));
        PathItem item = new PathItem().get(new Operation()
                .operationId("listPets")
                .responses(new ApiResponses().addApiResponse("200", ok)));
        doc.getPaths().addPathItem("/pets", item);
        doc.setComponents(new Components()
                .addSchemas("Pet", new Schema().typesItem("object"))
                .addSchemas("Unused", new Schema().typesItem("object")));
        OpenAPI filtered = new SpecFilter().filter(doc, new AbstractSpecFilter() {
            @Override
            public boolean isRemovingUnreferencedDefinitions() {
                return true;
            }
        }, null, null, null);
        assertNotNull(filtered.getComponents().getSchemas().get("Pet"),
                "schema referenced alongside a MediaType $ref must not be pruned");
    }

    @Test
    public void defaultMappingRefSurvivesAlongsideSchemaRef() {
        // a schema $ref must not hide a discriminator.defaultMapping sitting next to it
        OpenAPI doc = buildDoc();
        Schema pet = new Schema().$ref("#/components/schemas/Base");
        pet.setDiscriminator(new Discriminator().propertyName("kind")
                .defaultMapping("#/components/schemas/Cat"));
        doc.setComponents(new Components()
                .addSchemas("Pet", pet)
                .addSchemas("Base", new Schema().typesItem("object"))
                .addSchemas("Cat", new Schema().typesItem("object")));
        Content content = new Content().addMediaType("application/json",
                new MediaType().schema(new Schema().$ref("#/components/schemas/Pet")));
        doc.getPaths().get("/pets").getQuery().getResponses().get("200").content(content);
        OpenAPI filtered = new SpecFilter().filter(doc, new AbstractSpecFilter() {
            @Override
            public boolean isRemovingUnreferencedDefinitions() {
                return true;
            }
        }, null, null, null);
        assertNotNull(filtered.getComponents().getSchemas().get("Cat"),
                "schema referenced via defaultMapping next to a $ref must not be pruned");
    }

    @Test
    public void filterComponentsSchemaOverrideStillInvoked() {
        // subclasses overriding the five-argument filterComponentsSchema must keep
        // participating after the version-aware clone was introduced
        final boolean[] called = {false};
        SpecFilter custom = new SpecFilter() {
            @Override
            protected java.util.Map<String, Schema> filterComponentsSchema(
                    io.swagger.v3.core.filter.OpenAPISpecFilter filter,
                    java.util.Map<String, Schema> schemasMap,
                    java.util.Map<String, List<String>> params,
                    java.util.Map<String, String> cookies,
                    java.util.Map<String, List<String>> headers) {
                called[0] = true;
                return super.filterComponentsSchema(filter, schemasMap, params, cookies, headers);
            }
        };
        OpenAPI doc = buildDoc();
        doc.setComponents(new Components().addSchemas("Pet", new Schema().typesItem("object")));
        OpenAPI filtered = custom.filter(doc, new NoOpOperationsFilter(), null, null, null);
        assertTrue(called[0], "overridden filterComponentsSchema must be invoked");
        assertNotNull(filtered.getComponents().getSchemas().get("Pet"));
    }

    @Test
    public void explicit30VersionClonesWith30Mapper() {
        // an explicit openapi: 3.0.x is authoritative even when specVersion was left
        // inconsistent; the schema must be cloned through the 3.0 mapper
        OpenAPI doc = new OpenAPI()
                .openapi("3.0.3")
                .specVersion(SpecVersion.V32)
                .info(new Info().title("t").version("1"))
                .paths(new Paths());
        // 'nullable' only survives a 3.0 clone; 3.1+/3.2 mappers drop it
        doc.setComponents(new Components()
                .addSchemas("S", new Schema().type("string").nullable(true)));
        OpenAPI filtered = new SpecFilter().filter(doc, new NoOpOperationsFilter(), null, null, null);
        Schema filteredSchema = filtered.getComponents().getSchemas().get("S");
        assertNotNull(filteredSchema);
        assertEquals(filteredSchema.getNullable(), Boolean.TRUE,
                "a 3.0 document must be cloned through the 3.0 mapper, keeping 'nullable'");
    }
}
