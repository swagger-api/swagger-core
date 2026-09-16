package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.resources.APIResponsesResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class APIResponsesResourceTest {

    @Test
    public void testBooleanResponseSchema31() {
        OpenAPI openAPI = new Reader(new SwaggerConfiguration().openAPI31(true))
                .read(APIResponsesResource.class);

        Schema responseSchema = openAPI.getPaths()
                .get("/mypath")
                .getPost()
                .getResponses()
                .get("200")
                .getContent()
                .get("*/*")
                .getSchema();

        assertNull(responseSchema.getType());
        assertEquals(responseSchema.getTypes(), Collections.singleton("boolean"));
        SerializationMatchers.assertEqualsToJson31(responseSchema, "{\"type\":\"boolean\"}");
    }
}
