package io.swagger.v3.jaxrs2;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.jaxrs2.resources.PurgeMethodResource;
import io.swagger.v3.jaxrs2.resources.QueryMethodResource;
import io.swagger.v3.jaxrs2.resources.QueryStringParamResource;
import io.swagger.v3.jaxrs2.resources.QueryStringScopedResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Verifies that the reader maps a custom {@code @HttpMethod("QUERY")} annotation
 * (OpenAPI 3.2 query method) onto {@link PathItem#getQuery()}.
 */
public class QueryMethodTest {

    @Test(description = "read a resource declaring the OpenAPI 3.2 QUERY method")
    public void testQueryHttpMethod() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(QueryMethodResource.class);

        PathItem pathItem = openAPI.getPaths().get("/pets/search");
        assertNotNull(pathItem);

        assertNotNull(pathItem.getQuery());
        assertEquals(pathItem.getQuery().getOperationId(), "searchPets");

        // the operation must land on query only, not leak onto other methods
        assertNull(pathItem.getGet());
        assertNull(pathItem.getPost());
    }

    @Test(description = "a QUERY operation must not reach OpenAPI 3.0 or 3.1 output")
    public void testQueryHttpMethodIsNotSerializedBeforeOpenAPI32() throws Exception {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(QueryMethodResource.class);

        // "query" is a Path Item fixed field only as of OpenAPI 3.2, so the operation the
        // reader resolved stays in the model but is gated out of pre-3.2 documents
        for (String serialized : new String[]{
                Yaml.mapper().writeValueAsString(openAPI),
                Yaml31.mapper().writeValueAsString(openAPI),
                Json.mapper().writeValueAsString(openAPI),
                Json31.mapper().writeValueAsString(openAPI)}) {
            assertFalse(serialized.contains("query"));
            assertFalse(serialized.contains("searchPets"));
        }
    }

    @Test(description = "a custom HTTP method lands in additionalOperations with its declared case")
    public void testCustomHttpMethodGoesToAdditionalOperations() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(PurgeMethodResource.class);

        PathItem pathItem = openAPI.getPaths().get("/cache/entries");
        assertNotNull(pathItem);

        assertNotNull(pathItem.getAdditionalOperations(), "custom method must be kept in additionalOperations");
        assertTrue(pathItem.getAdditionalOperations().containsKey("PURGE"),
                "additionalOperations key must preserve the declared case");
        assertEquals(pathItem.getAdditionalOperations().get("PURGE").getOperationId(), "purgeCache");

        // and it must not be silently folded into any fixed operation slot
        assertNull(pathItem.getGet());
        assertNull(pathItem.getPost());
        assertNull(pathItem.getDelete());
    }

    @Test(description = "custom-method operations must not reach OpenAPI 3.0 or 3.1 output")
    public void testCustomHttpMethodIsNotSerializedBeforeOpenAPI32() throws Exception {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(PurgeMethodResource.class);

        for (String serialized : new String[]{
                Yaml.mapper().writeValueAsString(openAPI),
                Yaml31.mapper().writeValueAsString(openAPI),
                Json.mapper().writeValueAsString(openAPI),
                Json31.mapper().writeValueAsString(openAPI)}) {
            assertFalse(serialized.contains("purgeCache"));
            assertFalse(serialized.contains("PURGE"));
        }
    }

    @Test(description = "in: querystring is emitted only for OpenAPI 3.2 scans")
    public void testQueryStringParameterIsVersionGated() {
        QueryStringParamResource resource = new QueryStringParamResource();

        // a 3.2 scan keeps the parameter
        SwaggerConfiguration config32 = new SwaggerConfiguration()
                .openAPI31(true)
                .openAPIVersion("3.2.0");
        OpenAPI openAPI32 = new Reader(config32).read(resource.getClass());
        PathItem pathItem32 = openAPI32.getPaths().get("/search");
        assertNotNull(pathItem32.getGet());
        assertNotNull(pathItem32.getGet().getParameters());
        assertEquals(pathItem32.getGet().getParameters().get(0).getIn(), "querystring");

        // a 3.1 scan drops it rather than emitting an unrepresentable 'in' value
        SwaggerConfiguration config31 = new SwaggerConfiguration().openAPI31(true);
        OpenAPI openAPI31 = new Reader(config31).read(resource.getClass());
        PathItem pathItem31 = openAPI31.getPaths().get("/search");
        assertNotNull(pathItem31.getGet());
        assertTrue(pathItem31.getGet().getParameters() == null
                        || pathItem31.getGet().getParameters().stream()
                        .noneMatch(p -> "querystring".equals(p.getIn())),
                "in: querystring must not be emitted for a non-3.2 document");
    }

    @Test(description = "constructor and field level in: querystring see the scan's spec version")
    public void testQueryStringParameterIsVersionGatedForCtorAndFieldParams() {
        SwaggerConfiguration config32 = new SwaggerConfiguration()
                .openAPI31(true)
                .openAPIVersion("3.2.0");
        OpenAPI openAPI32 = new Reader(config32).read(QueryStringScopedResource.class);
        PathItem pathItem32 = openAPI32.getPaths().get("/ctorsearch");
        assertNotNull(pathItem32.getGet());
        assertNotNull(pathItem32.getGet().getParameters());
        assertTrue(pathItem32.getGet().getParameters().stream()
                        .anyMatch(p -> "querystring".equals(p.getIn())),
                "3.2 scan must keep querystring parameters from ctor/field annotations");

        SwaggerConfiguration config31 = new SwaggerConfiguration().openAPI31(true);
        OpenAPI openAPI31 = new Reader(config31).read(QueryStringScopedResource.class);
        PathItem pathItem31 = openAPI31.getPaths().get("/ctorsearch");
        assertNotNull(pathItem31.getGet());
        assertTrue(pathItem31.getGet().getParameters() == null
                        || pathItem31.getGet().getParameters().stream()
                        .noneMatch(p -> "querystring".equals(p.getIn())),
                "non-3.2 scan must drop querystring parameters from ctor/field annotations");
    }
}
