package io.swagger.v3.jaxrs2.annotations.examples;

import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.resources.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import static org.testng.Assert.assertEquals;

public class ExamplesTest extends AbstractAnnotationTest {


    @Test
    public void testRequestBodyContentExample() {
        compareToYamlFile(ExamplesTest.RequestBodyContentExample.class, "examples/");
        compareToYamlFile(ExamplesTest.RequestBodyContentExampleWithConsumes.class, "examples/");
        compareToYamlFile(ExamplesTest.RequestBodyContentExampleWithMediatype.class, "examples/");
        compareToYamlFile(ExamplesTest.RequestBodyContentExampleWithMediatype.class, "examples/");
        compareToYamlFile(ExamplesTest.RequestBodyContentExampleWithSchema.class, "examples/");
        compareToYamlFile(ExamplesTest.RequestBodyContentExampleWithSchemaImplementation.class, "examples/");
    }

    @Test
    public void testResponseExample() {
        compareToYamlFile(ExamplesTest.ResponseExample.class, "examples/");
        compareToYamlFile(ExamplesTest.ResponseExampleSchema.class, "examples/");
        compareToYamlFile(ExamplesTest.ResponseExampleSchemaImplementation.class, "examples/");
    }

    @Test
    public void testParameterExample() {
        compareToYamlFile(ExamplesTest.ParameterExample.class, "examples/");
    }


    @Test
    public void testAnnotatedModel() {
        compareToYamlFile(ExamplesTest.AnnotatedModelExample.class, "examples/");
        compareToYamlFile(ExamplesTest.AnnotatedModelAndContentExample.class, "examples/");
    }

    static class ResponseExampleSchema {
        @Path("/test")
        @POST
        @Operation(
                operationId = "subscribe",
                description = "subscribes a client to updates relevant to the requestor's account, as " +
                        "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
                responses = {
                        @ApiResponse(
                                description = "test description",
                                content = @Content(
                                        mediaType = "*/*",
                                        schema = @Schema(
                                                type = "string",
                                                format = "uuid",
                                                description = "the generated UUID",
                                                accessMode = Schema.AccessMode.READ_ONLY,
                                                example = "Schema example"
                                        ),
                                        examples = {
                                                @ExampleObject(
                                                        name = "Default Response",
                                                        value = "SubscriptionResponse",
                                                        summary = "Subscription Response Example"
                                                )
                                        }
                                ))
                })
        public ExamplesTest.SubscriptionResponse subscribe() {
            return null;
        }
    }

    static class ResponseExampleSchemaImplementation {
        @Path("/test")
        @POST
        @Operation(
                operationId = "subscribe",
                description = "subscribes a client to updates relevant to the requestor's account, as " +
                        "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
                responses = {
                        @ApiResponse(
                                description = "test description",
                                content = @Content(
                                        mediaType = "*/*",
                                        schema = @Schema(
                                                implementation = Subscription.class
                                        ),
                                        examples = {
                                                @ExampleObject(
                                                        name = "Default Response",
                                                        value = "{\"subscriptionId\" : \"1\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"2\"}}",
                                                        summary = "Subscription Response Example"
                                                )
                                        }
                                ))
                })
        public ExamplesTest.SubscriptionResponse subscribe() {
            return null;
        }
    }

    static class ResponseExample {
        @Path("/test")
        @POST
        @Operation(
                operationId = "subscribe",
                description = "subscribes a client to updates relevant to the requestor's account, as " +
                        "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
                responses = {
                        @ApiResponse(
                                description = "test description",
                                content = @Content(
                                        mediaType = "*/*",
                                        examples = {
                                                @ExampleObject(
                                                        name = "Default Response",
                                                        value = "{\"subscriptionId\" : \"1\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"2\"}}",
                                                        summary = "Subscription Response Example"
                                                )
                                        }
                                ))
                })
        public ExamplesTest.SubscriptionResponse subscribe() {
            return null;
        }
    }

    static class ParameterExample{
        @Path("/test")
        @POST
        public ExamplesTest.Subscription testRequestBody(
                @Parameter(
                        in = ParameterIn.QUERY,
                        description = "subscription param",
                        required = true,
                        example = "1",
                        schema = @Schema(
                                type = "string",
                                description = "the Sub",
                                accessMode = Schema.AccessMode.READ_ONLY,
                                example = "Schema example"
                        ),
                        content = @Content(
                                mediaType = "application/json",
                                examples = {
                                        @ExampleObject(
                                                name = "Default Request",
                                                value = "{\"subscriptionId\" : \"1\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"2\"}}",
                                                summary = "Subscription  Example")
                                }
                        ),
                        examples = {
                                @ExampleObject(
                                        name = "ex 1",
                                        value = "{\"subscriptionId\" : \"1\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"2\"}}"
                                ),
                                @ExampleObject(
                                        name = "ex 2",
                                        value = "{\"subscriptionId\" : \"12\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"22\"}}"
                                )
                        }
                ) String subId) {
            return null;
        }
    }


    static class RequestBodyContentExample{
        @Path("/test")
        @POST
        public ExamplesTest.Subscription testRequestBody(
                @RequestBody(
                        description = "Created user object",
                        required = true,
                        content = @Content(
                                examples = {
                                        @ExampleObject(
                                                name = "Default Request",
                                                value = "{\"subscriptionId\" : \"1\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"2\"}}",
                                                summary = "Subscription  Example")
                                }
                        )
                ) ExamplesTest.Subscription sub) {
            return null;
        }
    }


    static class RequestBodyContentExampleWithSchemaImplementation{

        @Path("/test")
        @POST
        public ExamplesTest.Subscription testRequestBody(
                @RequestBody(
                        description = "Created user object",
                        required = true,
                        content = @Content(
                            schema = @Schema(
                                implementation = ExamplesTest.Subscription.class
                            ),
                            examples = {
                                @ExampleObject(
                                        name = "Default Request",
                                        value = "{\"subscriptionId\" : \"1\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"2\"}}",
                                        summary = "Subscription  Example")
                            }
                        )
                ) ExamplesTest.Subscription sub) {
            return null;
        }

    }

    static class RequestBodyContentExampleWithSchema{

        @Path("/test")
        @POST
        public ExamplesTest.Subscription testRequestBody(
                @RequestBody(
                        description = "Created user object",
                        required = true,
                        content = @Content(
                                schema = @Schema(
                                        type = "string",
                                        description = "the Sub",
                                        accessMode = Schema.AccessMode.READ_ONLY,
                                        example = "Schema example"
                                ),
                                examples = {
                                        @ExampleObject(
                                                name = "Default Request",
                                                value = "3",
                                                summary = "Subscription  Example")
                                }
                        )
                ) ExamplesTest.Subscription sub) {
            return null;
        }

    }

    static class RequestBodyContentExampleWithMediatype{

        @Path("/test")
        @POST
        public ExamplesTest.Subscription testRequestBody(
                @RequestBody(
                        description = "Created user object",
                        required = true,
                        content = @Content(
                                mediaType = "application/json",
                                examples = {
                                        @ExampleObject(
                                                name = "Default Request",
                                                value = "{\"subscriptionId\" : \"1\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"2\"}}",
                                                summary = "Subscription  Example")
                                }
                        )
                ) ExamplesTest.Subscription sub) {
            return null;
        }

    }
    static class RequestBodyContentExampleWithConsumes{

        @Path("/test")
        @POST
        @Consumes("application/json")
        public ExamplesTest.Subscription testRequestBody(
                @RequestBody(
                        description = "Created user object",
                        required = true,
                        content = @Content(
                                examples = {
                                        @ExampleObject(
                                                name = "Default Request",
                                                value = "{\"subscriptionId\" : \"1\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"2\"}}",
                                                summary = "Subscription  Example")
                                }
                        )
                ) ExamplesTest.Subscription sub) {
            return null;
        }

    }

    static class AnnotatedModelExample{

        @Path("/test")
        @POST
        public ExamplesTest.AnnotatedSubscription testAnnotatedModel(
                @RequestBody(
                        description = "Created user object",
                        required = true
                ) ExamplesTest.AnnotatedSubscription sub) {
            return null;
        }

    }

    static class AnnotatedModelAndContentExample{

        @Path("/test")
        @POST
        public ExamplesTest.AnnotatedSubscription testAnnotatedModel(
                @RequestBody(
                        description = "Created user object",
                        required = true,
                        content = @Content(
                                mediaType = "application/json",
                                examples = {
                                        @ExampleObject(
                                                name = "Default Request",
                                                value = "{\"subscriptionId\" : \"1\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"2\"}}",
                                                summary = "Subscription  Example"),
                                        @ExampleObject(
                                                name = "Default Request 2",
                                                value = "{\"subscriptionId\" : \"12\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"22\"}}",
                                                summary = "Subscription  Example 2")
                                }
                        )
                ) ExamplesTest.AnnotatedSubscription sub) {
            return null;
        }

    }



    @Test
    public void testFullExample() {
        String openApiYAML = readIntoYaml(ExamplesTest.SimpleOperations.class);
        int start = openApiYAML.indexOf("/test:");
        int end = openApiYAML.length() - 1;
        String extractedYAML = openApiYAML.substring(start, end);
        String expectedYAML = "/test:\n" +
                "    post:\n" +
                "      description: subscribes a client to updates relevant to the requestor's account,\n" +
                "        as identified by the input token.  The supplied url will be used as the delivery\n" +
                "        address for response payloads\n" +
                "      operationId: subscribe\n" +
                "      parameters:\n" +
                "      - name: subscriptionId\n" +
                "        in: path\n" +
                "        required: true\n" +
                "        style: simple\n" +
                "        schema:\n" +
                "          $ref: '#/components/schemas/SubscriptionResponse'\n" +
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
                "          description: test description\n" +
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
                "    User:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        username:\n" +
                "          type: string\n" +
                "        firstName:\n" +
                "          type: string\n" +
                "        lastName:\n" +
                "          type: string\n" +
                "        email:\n" +
                "          type: string\n" +
                "        password:\n" +
                "          type: string\n" +
                "        phone:\n" +
                "          type: string\n" +
                "        userStatus:\n" +
                "          type: integer\n" +
                "          description: User Status\n" +
                "          format: int32\n" +
                "      xml:\n" +
                "        name: User\n" +
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
                        @Parameter(in = ParameterIn.PATH, name = "subscriptionId", required = true,
                                schema = @Schema(implementation = ExamplesTest.SubscriptionResponse.class),
                                style = ParameterStyle.SIMPLE, example = "example",
                                examples = {
                                        @ExampleObject(name = "subscriptionId_1", value = "12345",
                                                summary = "Subscription number 12345", externalValue = "Subscription external value 1"),
                                        @ExampleObject(name = "subscriptionId_2", value = "54321",
                                                summary = "Subscription number 54321", externalValue = "Subscription external value 2")
                                })
                },
                responses = {
                        @ApiResponse(
                                description = "test description",
                                content = @Content(
                                        mediaType = "*/*",
                                        schema = @Schema(
                                                type = "string",
                                                format = "uuid",
                                                description = "the generated UUID",
                                                accessMode = Schema.AccessMode.READ_ONLY,
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
                                accessMode = Schema.AccessMode.READ_ONLY,
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

    static class Subscription {
        public String subscriptionId;
        public SubscriptionItem subscriptionItem;
    }

    static class SubscriptionItem {
        public String subscriptionItemId;
    }

    @Schema(example = "{\"subscriptionId\" : \"1\", \"subscriptionItem\" : {\"subscriptionItemId\" : \"2\"}}")
    static class AnnotatedSubscription {
        public String subscriptionId;
        public AnnotatedSubscriptionItem subscriptionItem;
    }

    @Schema(example = "{\"subscriptionItemId\" : \"1\"}")
    static class AnnotatedSubscriptionItem {
        public String subscriptionItemId;
    }

}
