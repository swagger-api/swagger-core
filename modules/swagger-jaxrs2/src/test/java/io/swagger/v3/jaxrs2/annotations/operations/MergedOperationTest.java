package io.swagger.v3.jaxrs2.annotations.operations;

import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import static org.testng.Assert.assertEquals;

public class MergedOperationTest extends AbstractAnnotationTest {
    @Test(description = "shows a response when no annotation is present")
    public void testUnannotatedMethod() {
        String yaml = readIntoYaml(UnannotatedMethodClass.class);
        String expectedYaml = "openapi: 3.0.1\n";
        assertEquals(yaml, expectedYaml);
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

    @Test(description = "shows how a method with parameters and no special annotations is processed")
    public void testAnnotatedParameters() {
        String yaml = readIntoYaml(MethodWithParameters.class);

        assertEquals(yaml,
                "openapi: 3.0.1\n" +
                        "paths:\n" +
                        "  /findAll:\n" +
                        "    get:\n" +
                        "      description: method with parameters\n" +
                        "      operationId: getSimpleResponseWithParameters\n" +
                        "      parameters:\n" +
                        "      - name: id\n" +
                        "        in: query\n" +
                        "        schema:\n" +
                        "          type: string\n" +
                        "      - name: x-authorized-by\n" +
                        "        in: header\n" +
                        "        schema:\n" +
                        "          type: array\n" +
                        "          items:\n" +
                        "            type: string\n" +
                        "      responses:\n" +
                        "        default:\n" +
                        "          description: default response\n" +
                        "          content:\n" +
                        "            '*/*':\n" +
                        "              schema:\n" +
                        "                $ref: '#/components/schemas/SimpleResponse'\n" +
                        "components:\n" +
                        "  schemas:\n" +
                        "    SimpleResponse:\n" +
                        "      type: object\n");
    }

    static class MethodWithParameters {
        @GET
        @Operation(description = "method with parameters")
        @Path("/findAll")
        public SimpleResponse getSimpleResponseWithParameters(
                @QueryParam("id") String id,
                @HeaderParam("x-authorized-by") String[] auth) {
            return null;
        }
    }

    @Test(description = "shows how annotations can decorate an operation")
    public void testPartiallyAnnotatedMethod() {
        String yaml = readIntoYaml(MethodWithPartialAnnotation.class);
        String expectedYaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /findAll:\n" +
                "    get:\n" +
                "      description: returns a value\n" +
                "      operationId: getSimpleResponseWithParameters\n" +
                "      parameters:\n" +
                "      - name: id\n" +
                "        in: query\n" +
                "        schema:\n" +
                "          pattern: '[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}'\n" +
                "          type: string\n" +
                "          description: a GUID for the user in uuid-v4 format\n" +
                "          format: uuid\n" +
                "      - name: x-authorized-by\n" +
                "        in: header\n" +
                "        schema:\n" +
                "          type: array\n" +
                "          items:\n" +
                "            type: string\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: default response\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SimpleResponse'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SimpleResponse:\n" +
                "      type: object\n";

        assertEquals(yaml, expectedYaml);
    }

    static class MethodWithPartialAnnotation {
        @GET
        @Operation(description = "returns a value")
        @Path("/findAll")
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

    @Test(description = "shows how a request body is passed")
    public void testRequestBody() {
        String yaml = readIntoYaml(MethodWithRequestBody.class);
        String expectedYaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /add:\n" +
                "    post:\n" +
                "      description: receives a body\n" +
                "      operationId: addValue\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/InputValue'\n" +
                "      responses:\n" +
                "        201:\n" +
                "          description: value successfully processed\n" +
                "components:\n" +
                "  schemas:\n" +
                "    InputValue:\n" +
                "      type: object\n";

        assertEquals(yaml, expectedYaml);
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
