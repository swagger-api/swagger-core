package io.swagger.v3.jaxrs2;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.jaxrs2.resources.QueryMethodResource;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

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
}
