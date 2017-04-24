package io.swagger.annotations.test.operations;

import io.swagger.annotations.OASOperation;
import io.swagger.annotations.media.OASContent;
import io.swagger.annotations.media.OASExampleObject;
import io.swagger.annotations.media.OASSchema;
import io.swagger.annotations.responses.OASResponse;
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
        @OASOperation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with no inputs and a complex output object",
            operationId = "getWithNoParameters",
            deprecated = true,
            responses = {
                @OASResponse(
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
        @OASOperation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with no inputs and a complex output object",
            operationId = "getWithPayloadResponse",
            deprecated = true,
            responses = {
                @OASResponse(
                    responseCode = "200",
                    description = "voila!",
                    content = @OASContent(
                        mediaType = "application/json",
                        schema = @OASSchema(implementation = SampleResponseSchema.class)
                    )
                ),
                @OASResponse(
                    responseCode = "default",
                    description = "boo",
                    content = @OASContent(
                        mediaType = "*/*",
                        schema = @OASSchema(implementation = GenericError.class)
                    )
                )
            }
        )
        public void simpleGet() {}
    }

    static class SampleResponseSchema {
        @OASSchema(description = "the user id")
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
        @OASOperation(
            summary = "Simple get operation",
            description = "Defines a simple get operation with no inputs and a complex output object",
            operationId = "getWithPayloadResponse",
            deprecated = true,
            responses = {
                @OASResponse(
                    responseCode = "200",
                    description = "voila!",
                    content = @OASContent(
                        mediaType = "application/json",
                        schema = @OASSchema(implementation = SampleResponseSchema.class),
                        examples = {
                            @OASExampleObject(
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
