package io.swagger.v3.jaxrs2.annotations.parameters;

import com.google.common.collect.Sets;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import io.swagger.v3.jaxrs2.resources.ResourceWithJacksonBean;
import io.swagger.v3.jaxrs2.resources.ResourceWithKnownInjections;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ParametersTest extends AbstractAnnotationTest {

    @Test(description = "scan class level and field level annotations")
    public void scanClassAndFieldLevelAnnotations() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ResourceWithKnownInjections.class);
        Yaml.prettyPrint(openAPI);
        List<io.swagger.v3.oas.models.parameters.Parameter> resourceParameters = openAPI.getPaths().get("/resource/{id}").getGet().getParameters();
        assertNotNull(resourceParameters);
        assertEquals(resourceParameters.size(), 3);
        assertEquals(resourceParameters.get(0).getName(), "id");
        assertEquals(resourceParameters.get(1).getName(), "fieldParam");
        assertEquals(resourceParameters.get(2).getName(), "methodParam");
    }

    @Test
    public void testParameters() {
        String openApiYAML = readIntoYaml(ParametersTest.SimpleOperations.class);
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
                "      - name: formId\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "        example: Example\n" +
                "      - name: explodeFalse\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        explode: false\n" +
                "        schema:\n" +
                "          $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "      - name: explodeTrue\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        explode: true\n" +
                "        schema:\n" +
                "          $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "      - name: explodeAvoiding\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          description: the generated id\n" +
                "          format: id\n" +
                "          readOnly: true\n" +
                "      - name: arrayParameter\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: number\n" +
                "              description: the generated id\n" +
                "              readOnly: true\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              type: number\n" +
                "              description: the generated id\n" +
                "              readOnly: true\n" +
                "      - name: arrayParameterImplementation\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          maxItems: 10\n" +
                "          minItems: 1\n" +
                "          uniqueItems: true\n" +
                "          type: array\n" +
                "          items:\n" +
                "            $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "      - name: arrayParameterImplementation2\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        explode: true\n" +
                "        schema:\n" +
                "          $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: test description\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SubscriptionResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        subscriptionId:\n" +
                "          type: string";
        assertEquals(extractedYAML, expectedYAML);
    }

    @Test
    public void testArraySchemaParameters() {
        String openApiYAML = readIntoYaml(ParametersTest.ArraySchemaOperations.class);
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
                "      - name: arrayParameter\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          maxItems: 10\n" +
                "          minItems: 1\n" +
                "          uniqueItems: true\n" +
                "          type: array\n" +
                "          items:\n" +
                "            $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: test description\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SubscriptionResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        subscriptionId:\n" +
                "          type: string";
        assertEquals(extractedYAML, expectedYAML);
    }

    @Test
    public void testRepeatableParameters() {
        String openApiYAML = readIntoYaml(ParametersTest.RepeatableParametersOperations.class);
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
                "      - name: formId\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "        example: Example\n" +
                "      - name: explodeFalse\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        explode: false\n" +
                "        schema:\n" +
                "          $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "      - name: explodeTrue\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        explode: true\n" +
                "        schema:\n" +
                "          $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "      - name: explodeAvoiding\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "          description: the generated id\n" +
                "          format: id\n" +
                "          readOnly: true\n" +
                "      - name: arrayParameter\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              type: number\n" +
                "              description: the generated id\n" +
                "              readOnly: true\n" +
                "          application/xml:\n" +
                "            schema:\n" +
                "              type: number\n" +
                "              description: the generated id\n" +
                "              readOnly: true\n" +
                "      - name: arrayParameterImplementation\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          maxItems: 10\n" +
                "          minItems: 1\n" +
                "          uniqueItems: true\n" +
                "          type: array\n" +
                "          items:\n" +
                "            $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "      - name: arrayParameterImplementation2\n" +
                "        in: query\n" +
                "        required: true\n" +
                "        explode: true\n" +
                "        schema:\n" +
                "          $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "      responses:\n" +
                "        default:\n" +
                "          description: test description\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SubscriptionResponse'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SubscriptionResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        subscriptionId:\n" +
                "          type: string";
        assertEquals(extractedYAML, expectedYAML);
    }

    @Test(description = "JsonUnwrapped, JsonIgnore, JsonValue should be honoured")
    public void testJacksonFeatures() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(ResourceWithJacksonBean.class);
        Yaml.prettyPrint(openAPI);
        io.swagger.v3.oas.models.media.Schema o = openAPI.getComponents().getSchemas().get("JacksonBean");

        assertEquals(o.getProperties().keySet(), Sets.newHashSet("identity", "bean", "code", "message",
                "precodesuf", "premessagesuf"));
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
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class), style = ParameterStyle.SIMPLE),
                        @Parameter(in = ParameterIn.QUERY, name = "formId", required = true,
                                example = "Example"),
                        @Parameter(in = ParameterIn.QUERY, name = "explodeFalse", required = true, explode = Explode.FALSE,
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class)),
                        @Parameter(in = ParameterIn.QUERY, name = "explodeTrue", required = true, explode = Explode.TRUE,
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class)),
                        @Parameter(in = ParameterIn.QUERY, name = "explodeAvoiding", required = true, explode = Explode.TRUE,
                                schema = @Schema(
                                        type = "int",
                                        format = "id",
                                        description = "the generated id",
                                        accessMode = Schema.AccessMode.READ_ONLY
                                )),
                        @Parameter(in = ParameterIn.QUERY, name = "arrayParameter", required = true, explode = Explode.TRUE,
                                array = @ArraySchema(maxItems = 10, minItems = 1,
                                        schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class),
                                        uniqueItems = true
                                )
                                ,
                                schema = @Schema(
                                        type = "int",
                                        format = "id",
                                        description = "the generated id",
                                        accessMode = Schema.AccessMode.READ_ONLY),
                                content = @Content(schema = @Schema(type = "number",
                                        description = "the generated id",
                                        accessMode = Schema.AccessMode.READ_ONLY))
                        ),
                        @Parameter(in = ParameterIn.QUERY, name = "arrayParameterImplementation", required = true, explode = Explode.TRUE,
                                array = @ArraySchema(maxItems = 10, minItems = 1,
                                        schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class),
                                        uniqueItems = true
                                )
                        ),
                        @Parameter(in = ParameterIn.QUERY, name = "arrayParameterImplementation2", required = true, explode = Explode.TRUE,
                                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class))

                },
                responses = {
                        @ApiResponse(
                                description = "test description", content = @Content(
                                mediaType = "*/*",
                                schema =
                                @Schema(
                                        implementation = ParametersTest.SubscriptionResponse.class)
                        ))
                })
        @Consumes({"application/json", "application/xml"})
        public ParametersTest.SubscriptionResponse subscribe() {
            return null;
        }
    }

    static class ArraySchemaOperations {
        @Path("/test")
        @POST
        @Operation(
                operationId = "subscribe",
                description = "subscribes a client to updates relevant to the requestor's account, as " +
                        "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
                parameters = {
                        @Parameter(in = ParameterIn.QUERY, name = "arrayParameter", required = true, explode = Explode.TRUE,
                                array = @ArraySchema(maxItems = 10, minItems = 1,
                                        schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class),
                                        uniqueItems = true
                                )
                        ),
                },
                responses = {
                        @ApiResponse(
                                description = "test description", content = @Content(
                                mediaType = "*/*",
                                schema =
                                @Schema(
                                        implementation = ParametersTest.SubscriptionResponse.class)
                        ))
                })
        @Consumes({"application/json", "application/xml"})
        public ParametersTest.SubscriptionResponse subscribe() {
            return null;
        }
    }

    static class RepeatableParametersOperations {
        @Path("/test")
        @POST
        @Parameter(in = ParameterIn.PATH, name = "subscriptionId", required = true,
                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class), style = ParameterStyle.SIMPLE)
        @Parameter(in = ParameterIn.QUERY, name = "formId", required = true,
                example = "Example")
        @Parameter(in = ParameterIn.QUERY, name = "explodeFalse", required = true, explode = Explode.FALSE,
                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class))
        @Parameter(in = ParameterIn.QUERY, name = "explodeTrue", required = true, explode = Explode.TRUE,
                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class))
        @Parameter(in = ParameterIn.QUERY, name = "explodeAvoiding", required = true, explode = Explode.TRUE,
                schema = @Schema(
                        type = "int",
                        format = "id",
                        description = "the generated id",
                        accessMode = Schema.AccessMode.READ_ONLY
                ))
        @Parameter(in = ParameterIn.QUERY, name = "arrayParameter", required = true, explode = Explode.TRUE,
                array = @ArraySchema(maxItems = 10, minItems = 1,
                        schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class),
                        uniqueItems = true
                )
                ,
                schema = @Schema(
                        type = "int",
                        format = "id",
                        description = "the generated id",
                        accessMode = Schema.AccessMode.READ_ONLY),
                content = @Content(schema = @Schema(type = "number",
                        description = "the generated id",
                        accessMode = Schema.AccessMode.READ_ONLY))
        )
        @Parameter(in = ParameterIn.QUERY, name = "arrayParameterImplementation", required = true, explode = Explode.TRUE,
                array = @ArraySchema(maxItems = 10, minItems = 1,
                        schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class),
                        uniqueItems = true
                )
        )
        @Parameter(in = ParameterIn.QUERY, name = "arrayParameterImplementation2", required = true, explode = Explode.TRUE,
                schema = @Schema(implementation = ParametersTest.SubscriptionResponse.class))
        @Operation(
                operationId = "subscribe",
                description = "subscribes a client to updates relevant to the requestor's account, as " +
                        "identified by the input token.  The supplied url will be used as the delivery address for response payloads",
                responses = {
                        @ApiResponse(
                                description = "test description", content = @Content(
                                mediaType = "*/*",
                                schema =
                                @Schema(
                                        implementation = ParametersTest.SubscriptionResponse.class)
                        ))
                })
        @Consumes({"application/json", "application/xml"})
        public ParametersTest.SubscriptionResponse subscribe() {
            return null;
        }
    }

    static class SubscriptionResponse {
        public String subscriptionId;
    }
}
