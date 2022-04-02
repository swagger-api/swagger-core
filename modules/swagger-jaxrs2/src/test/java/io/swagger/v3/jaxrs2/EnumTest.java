package io.swagger.v3.jaxrs2;

import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.jaxrs2.resources.EnumParameterResource;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.io.IOException;

public class EnumTest {

    @Test(description = "Test enum")
    public void testEnum() throws IOException {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(EnumParameterResource.class);
        SerializationMatchers.assertEqualsToYaml(openAPI, EXPECTED_YAML);
    }


    static final String EXPECTED_YAML = "openapi: 3.0.1\n" +
            "paths:\n" +
            "  /task:\n" +
            "    get:\n" +
            "      operationId: getTasks\n" +
            "      parameters:\n" +
            "      - name: guid\n" +
            "        in: query\n" +
            "        required: true\n" +
            "        schema:\n" +
            "          type: string\n" +
            "      - name: tasktype\n" +
            "        in: query\n" +
            "        schema:\n" +
            "          type: string\n" +
            "          enum:\n" +
            "          - A\n" +
            "          - B\n" +
            "      responses:\n" +
            "        \"200\":\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                type: array\n" +
            "                items:\n" +
            "                  $ref: '#/components/schemas/TaskDTO'\n" +
            "        \"404\":\n" +
            "          description: User not found\n" +
            "components:\n" +
            "  schemas:\n" +
            "    TaskDTO:\n" +
            "      type: object\n";
}