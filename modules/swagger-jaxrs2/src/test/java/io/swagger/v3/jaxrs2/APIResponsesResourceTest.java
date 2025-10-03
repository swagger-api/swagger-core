package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.resources.APIResponsesResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class APIResponsesResourceTest {
    private OpenAPI openAPI;

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

    @Test
    public void testStringOrEmailSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postStringOrEmailSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testBooleanSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postBooleanSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testByteOrBinarySchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postByteOrBinarySchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testURISchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postURISchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testURLSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postURLSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testUUIDSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postUUIDSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testIntegerSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postIntegerSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testLongSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postLongSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testFloatSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postFloatSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testDoubleSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postDoubleSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testBigIntegerSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postBigIntegerSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testBigDecimalSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postBigDecimalSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testNumberSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postNumberSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testDateStubSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postDateStubSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testDateSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postDateSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testLocalTimeSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postLocalTimeSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testFileSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postFileSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }

    @Test
    public void testObjectSchemaAPIResource31() {
        Schema responseSchema = getResponseSchema("/postObjectSchemaContent");

        // Value of field "type" must equal value of field "types"
        assertEquals(responseSchema.getType(), responseSchema.getTypes().iterator().next());
    }
}