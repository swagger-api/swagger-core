package io.swagger.v3.core.serialization;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Json32;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.core.util.Yaml32;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
}
