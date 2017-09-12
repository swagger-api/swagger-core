package io.swagger.jaxrs2.annotations.examples;

import io.swagger.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.jaxrs2.resources.model.User;
import io.swagger.oas.annotations.Operation;
import io.swagger.oas.annotations.Parameter;
import io.swagger.oas.annotations.media.Content;
import io.swagger.oas.annotations.media.ExampleObject;
import io.swagger.oas.annotations.media.Schema;
import io.swagger.oas.annotations.parameters.RequestBody;
import io.swagger.oas.annotations.responses.ApiResponse;
import org.testng.annotations.Test;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static org.testng.Assert.assertEquals;

/**
 * Created by rafaellopez on 9/8/17.
 */
public class ExamplesTest extends AbstractAnnotationTest {

    @Test
    public void testSimpleCallback() {
        String openApiYAML = readIntoYaml(ExamplesTest.SimpleOperations.class);
        int start = openApiYAML.indexOf("/test:");
        int end = openApiYAML.length() - 1;
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = "/test:\n" +
                "    post:\n" +
                "      description: subscribes a client to updates relevant to the requestor's account, as identified by the input token.  The supplied url will be used as the delivery address for response payloads\n" +
                "      operationId: subscribe\n" +
                "      parameters:\n" +
                "      - name: subscriptionId\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        style: simple\n" +
                "        examples:\n" +
                "          subscriptionId_2:\n" +
                "            summary: Subscription number 54321\n" +
                "            description: subscriptionId_2\n" +
                "            value: 54321\n" +
                "            externalValue: Subscription external value 2\n" +
                "          subscriptionId_1:\n" +
                "            summary: Subscription number 12345\n" +
                "            description: subscriptionId_1\n" +
                "            value: 12345\n" +
                "            externalValue: Subscription external value 1\n" +
                "        example: example\n" +
                "      requestBody:\n" +
                "        description: Created user object\n" +
                "        content:\n" +
                "          '*/*':\n" +
                "            schema:\n" +
                "              type: string\n" +
                "              description: the generated UUID\n" +
                "              format: uuid\n" +
                "              readOnly: true\n" +
                "              example: Schema example\n" +
                "            examples:\n" +
                "              Default Response:\n" +
                "                summary: Subscription Response Example\n" +
                "                description: Default Response\n" +
                "                value: SubscriptionResponse\n" +
                "                externalValue: Subscription Response value 1\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: no description\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: string\n" +
                "                description: the generated UUID\n" +
                "                format: uuid\n" +
                "                readOnly: true\n" +
                "                example: Schema example\n" +
                "              examples:\n" +
                "                Default Response:\n" +
                "                  summary: Subscription Response Example\n" +
                "                  description: Default Response\n" +
                "                  value: SubscriptionResponse\n" +
                "                  externalValue: Subscription Response value 1\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SubscriptionResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        subscriptionId:\n" +
                "          type: string";
        assertEquals(extractedYAML, expectedYAML);
    }

    static class SimpleOperations {
        @Path("/test")
        @POST
        @Operation(
                operationId = "subscribe",
                description = "subscribes a client to updates relevant to the requestor's account, as " +
                        "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
                parameters = {
                        @Parameter(in = "path", name = "subscriptionId", required = true,
                                schema = @Schema(implementation = ExamplesTest.SubscriptionResponse.class),
                                style = "simple", example = "example",
                                examples = {
                                        @ExampleObject(name = "subscriptionId_1", value = "12345",
                                                summary = "Subscription number 12345", externalValue = "Subscription external value 1"),
                                        @ExampleObject(name = "subscriptionId_2", value = "54321",
                                                summary = "Subscription number 54321", externalValue = "Subscription external value 2")
                                })
                },
                responses = {
                        @ApiResponse(
                                description = "no description",
                                content = @Content(
                                        mediaType = "*/*",
                                        schema = @Schema(
                                                type = "string",
                                                format = "uuid",
                                                description = "the generated UUID",
                                                readOnly = true,
                                                example = "Schema example"
                                        ),
                                        examples = {
                                                @ExampleObject(name = "Default Response", value = "SubscriptionResponse",
                                                        summary = "Subscription Response Example", externalValue = "Subscription Response value 1")
                                        }
                                ))
                })
        public ExamplesTest.SubscriptionResponse subscribe(@RequestBody(description = "Created user object", required = true,
                content = @Content(
                        schema = @Schema(
                                type = "string",
                                format = "uuid",
                                description = "the generated UUID",
                                readOnly = true,
                                example = "Schema example"),
                        examples = {
                                @ExampleObject(name = "Default Response", value = "SubscriptionResponse",
                                        summary = "Subscription Response Example", externalValue = "Subscription Response value 1")
                        })) User user) {
            return null;
        }
    }

    static class SubscriptionResponse {
        public String subscriptionId;
    }

}
