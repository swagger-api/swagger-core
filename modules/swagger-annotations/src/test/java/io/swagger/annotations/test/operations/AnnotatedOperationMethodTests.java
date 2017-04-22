package io.swagger.annotations.test.operations;

import io.swagger.annotations.Operation;
import io.swagger.annotations.media.Content;
import io.swagger.annotations.media.ExampleObject;
import io.swagger.annotations.media.Schema;
import io.swagger.annotations.responses.Response;
import io.swagger.annotations.test.AbstractAnnotationTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AnnotatedOperationMethodTests extends AbstractAnnotationTest {
    @Test
    public void testSimpleGetOperation() {
        String yaml = readIntoYaml(SimpleGetOperationTest.class);

        assertEquals(yaml,
            "get:\n" +
            "  description: 'Defines a simple get operation with no inputs and a complex output object'\n" +
            "  operationId: getWithNoParameters\n" +
            "  summary: 'Simple get operation'\n" +
            "  deprecated: true\n" +
            "  parameters: []\n" +
            "  responses:\n" +
            "    200:\n" +
            "      description: 'voila'");
    }

    static class SimpleGetOperationTest {
        @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with no inputs and a complex output object",
            operationId = "getWithNoParameters",
            deprecated = true,
            responses = {
                @Response(
                    responseCode = "200",
                    description = "voila!")
            }
        )
        public void simpleGet() {}
    }

    @Test
    public void testGetOperationWithResponsePayloadAndAlternateCodes () {
        String yaml = readIntoYaml(GetOperationWithResponsePayloadAndAlternateCodes.class);

        assertEquals(yaml,
            "get:\n" +
            "  description: 'Defines a simple get operation with no inputs and a complex output object'\n" +
            "  operationId: getWithPayloadResponse\n" +
            "  summary: 'Simple get operation'\n" +
            "  deprecated: true\n" +
            "  parameters: []\n" +
            "  responses:\n" +
            "    200:\n" +
            "      description: 'voila'\n" +
            "      content:" +
            "        application/json:" +
            "           schema:\n" +
            "             type: object\n" +
            "             properties:\n" +
            "               id:\n" +
            "                 type: string\n" +
            "                 description: 'the user id'\n" +
            "    default\n" +
            "      description: boo\n" +
            "      content:\n" +
            "        */*:" +
            "           description: boo");
    }

    static class GetOperationWithResponsePayloadAndAlternateCodes {
        @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with no inputs and a complex output object",
            operationId = "getWithPayloadResponse",
            deprecated = true,
            responses = {
                @Response(
                    responseCode = "200",
                    description = "voila!",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SampleResponseSchema.class)
                    )
                ),
                @Response(
                    responseCode = "default",
                    description = "boo",
                    content = @Content(
                        mediaType = "*/*",
                        schema = @Schema(implementation = GenericError.class)
                    )
                )
            }
        )
        public void simpleGet() {}
    }

    static class SampleResponseSchema {
        @Schema(description = "the user id")
        private String id;
    }

    static class GenericError {
        private int code;
        private String message;
    }

    @Test(enabled = false, description = "reads an operation with response examples defined")
    public void testOperationWithResponseExamples () {
        String yaml = readIntoYaml(GetOperationWithResponseExamples.class);

        assertEquals(yaml,
            "get:\n" +
            "  description: 'Defines a simple get operation with no inputs and a complex output object'\n" +
            "  operationId: getWithNoParameters\n" +
            "  summary: 'Simple get operation'\n" +
            "  deprecated: true\n" +
            "  parameters: []\n" +
            "  responses:\n" +
            "    200:\n" +
            "      description: 'voila'\n" +
            "      content:\n" +
            "        application/json:\n" +
            "          schema:\n" +
            "            type: object\n" +
            "            properties:\n" +
            "              id:\n" +
            "                type: string\n" +
            "          examples:\n" +
            "            basic:\n" +
            "              id: 19877734");

    }

    static class GetOperationWithResponseExamples {
        @Operation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with no inputs and a complex output object",
            operationId = "getWithPayloadResponse",
            deprecated = true,
            responses = {
                @Response(
                    responseCode = "200",
                    description = "voila!",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = SampleResponseSchema.class),
                        examples = {
                            @ExampleObject(
                                name = "basic",
                                summary = "shows a basic example",
                                value = "{\"id\": 19877734}")
                        }
                    )
                )
            }
        )
        public void simpleGet() {}
    }
}
