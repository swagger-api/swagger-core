package io.swagger.jaxrs2.annotations.operations;

import io.swagger.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.testng.Assert.assertEquals;

public class MergedOperationTest extends AbstractAnnotationTest {
    @Test(enabled = false, description = "shows a response when no annotation is present")
    public void testUnannotatedMethod() {
        String yaml = readIntoYaml(UnannotatedMethodClass.class);

        assertEquals(yaml,
                "get:\n" +
                        "  operationId: getSimpleResponse\n" +
                        "  parameters: []\n" +
                        "  responses:\n" +
                        "    default:\n" +
                        "      content:\n" +
                        "        */*:\n" +
                        "          schema:\n" +
                        "            type: object\n" +
                        "            properties:\n" +
                        "              id:\n" +
                        "                type: string\n");
    }

    static class UnannotatedMethodClass {
        @GET
        public SimpleResponse getSimpleResponse() {
            return null;
        }
    }

    static class SimpleResponse {
        private String id;
    }

    @Test(enabled = false, description = "shows how a method with parameters and no special annotations is processed")
    public void testAnnotatedParameters() {
        String yaml = readIntoYaml(MethodWithParameters.class);

        assertEquals(yaml,
                "get:\n" +
                        "  operationId: getSimpleResponseWithParameters\n" +
                        "  parameters:\n" +
                        "    - in: query\n" +
                        "      name: id\n" +
                        "      schema:\n" +
                        "        type: string\n" +
                        "    - in: header\n" +
                        "      name: x-authorized-by\n" +
                        "      style: simple\n" +
                        "      schema:\n" +
                        "        type: array\n" +
                        "        items:\n" +
                        "          type: string\n" +
                        "  responses:\n" +
                        "    default:\n" +
                        "      content:\n" +
                        "        */*:\n" +
                        "          schema:\n" +
                        "            type: object\n" +
                        "            properties:\n" +
                        "              id:\n" +
                        "                type: string");
    }

    static class MethodWithParameters {
        @GET
        public SimpleResponse getSimpleResponseWithParameters(
                @QueryParam("id") String id,
                @HeaderParam("x-authorized-by") String[] auth) {
            return null;
        }
    }

    @Test(enabled = false, description = "shows how annotations can decorate an operation")
    public void testPartiallyAnnotatedMethod() {
        String yaml = readIntoYaml(MethodWithPartialAnnotation.class);

        assertEquals(yaml,
                "get:\n" +
                        "  operationId: getSimpleResponseWithParameters\n" +
                        "  parameters:\n" +
                        "    - in: query\n" +
                        "      name: id\n" +
                        "      description: a GUID for th user in uuid-v4 format\n" +
                        "      required: true\n" +
                        "      schema:\n" +
                        "        type: string\n" +
                        "        format: uuid\n" +
                        "        pattern: '[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}'\n" +
                        "    - in: header\n" +
                        "      name: x-authorized-by\n" +
                        "      style: simple\n" +
                        "      schema:\n" +
                        "        type: array\n" +
                        "        items:\n" +
                        "          type: string\n" +
                        "  responses:\n" +
                        "    default:\n" +
                        "      content:\n" +
                        "        */*:\n" +
                        "          schema:\n" +
                        "            type: object\n" +
                        "            properties:\n" +
                        "              id:\n" +
                        "                type: string");
    }

    static class MethodWithPartialAnnotation {
        @GET
        @Operation(description = "returns a value")
        public SimpleResponse getSimpleResponseWithParameters(
                @Schema(
                        description = "a GUID for the user in uuid-v4 format",
                        required = true,
                        format = "uuid",
                        pattern = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")

                @QueryParam("id") String id,
                @HeaderParam("x-authorized-by") String[] auth) {
            return null;
        }
    }

    @Test(enabled = false, description = "shows how a request body is passed")
    public void testRequestBody() {
        String yaml = readIntoYaml(MethodWithRequestBody.class);

        assertEquals(yaml,
                "post:\n" +
                        "  operationId: addValue\n" +
                        "  description: receives a body\n" +
                        "  parameters:\n" +
                        "    - in: body\n" +
                        "      name: input\n" +
                        "      schema:\n" +
                        "        type: object\n" +
                        "        properties:\n" +
                        "          id:\n" +
                        "            type: integer\n" +
                        "            format: int64\n" +
                        "          name:\n" +
                        "            type: string\n" +
                        "  responses:\n" +
                        "    201:\n" +
                        "      description: value successfully processed");
    }

    static class MethodWithRequestBody {
        @POST
        @Operation(description = "receives a body",
                responses = @ApiResponse(
                        responseCode = "201",
                        description = "value successfully processed")
        )
        @Path("/add")
        public void addValue(InputValue input) {
        }
    }

    static class InputValue {
        private Long id;
        private String name;
    }
}
