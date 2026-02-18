package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.resources.APIResponsesResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class APIResponsesResourceTest {
    private OpenAPI openAPI;

    // Prepare list of resource schema URLs
    @DataProvider(name = "schemas")
    public String[] getSchemas() {
        return new String[] {
                "/postStringOrEmailSchemaContent",
                "/postBooleanSchemaContent",
                "/postByteOrBinarySchemaContent",
                "/postURISchemaContent",
                "/postURLSchemaContent",
                "/postUUIDSchemaContent",
                "/postIntegerSchemaContent",
                "/postLongSchemaContent",
                "/postFloatSchemaContent",
                "/postDoubleSchemaContent",
                "/postBigIntegerSchemaContent",
                "/postBigDecimalSchemaContent",
                "/postNumberSchemaContent",
                "/postDateStubSchemaContent",
                "/postDateSchemaContent",
                "/postLocalTimeSchemaContent",
                "/postFileSchemaContent",
                "/postObjectSchemaContent"
        };
    }

    @BeforeMethod
    public void setUp() {
        SwaggerConfiguration config = new SwaggerConfiguration().openAPI31(true);
        Reader reader = new Reader(config);
        openAPI = reader.read(APIResponsesResource.class);
    }

    private Schema getResponseSchema(String path) {
        Operation postOperation = openAPI.getPaths().get(path).getPost();
        return postOperation.getResponses().get("200").getContent().get("*/*").getSchema();
    }

    @Test(dataProvider = "schemas")
    public void testSchemaAPIResource31(String schema) {
        Schema responseSchema = getResponseSchema(schema);

        String fromGetType = responseSchema.getType() == null ? "null" : responseSchema.getType();
        String fromGetTypes = responseSchema.getTypes() == null ? "null" : responseSchema.getTypes().iterator().next().toString();

        // Value of field "type" must equal value of field "types"
        assertEquals(fromGetType, fromGetTypes);
    }
}