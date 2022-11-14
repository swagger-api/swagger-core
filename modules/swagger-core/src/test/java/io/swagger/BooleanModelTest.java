package io.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.ModelImpl;
import io.swagger.models.Swagger;
import io.swagger.util.Yaml;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class BooleanModelTest {
    @Test
    public void testBooleanProperty() throws JsonProcessingException {
        String yaml = "swagger: '2.0'\n" +
                "info:\n" +
                "  title: Some API\n" +
                "  description: >-\n" +
                "    This is the Some Api\n" +
                "  version: 2.0.0\n" +
                "basePath: /somepath\n" +
                "schemes:\n" +
                "  - https\n" +
                "consumes:\n" +
                "  - application/json\n" +
                "produces:\n" +
                "  - application/json\n" +
                "paths:\n" +
                "  /somepath:\n" +
                "    get:\n" +
                "      description: >\n" +
                "        my description\n" +
                "      operationId: MyGet\n" +
                "      responses:\n" +
                "        '200':\n" +
                "          $ref: '#/responses/Response'\n" +
                "responses:\n" +
                "  Response:\n" +
                "    description: Response\n" +
                "    schema:\n" +
                "      type: object\n" +
                "      required:\n" +
                "        - Report\n" +
                "      properties:\n" +
                "        Report:\n" +
                "          type: string\n" +
                "      additionalProperties: false";

        Swagger swagger = Yaml.mapper().readValue(yaml, Swagger.class);
        assertEquals(((ModelImpl)swagger.getResponses().get("Response").getResponseSchema()).getAdditionalProperties().getBooleanValue().booleanValue(), false);
        SerializationMatchers.assertEqualsToYaml(swagger, "swagger: \"2.0\"\n" +
                "info:\n" +
                "  description: \"This is the Some Api\"\n" +
                "  version: \"2.0.0\"\n" +
                "  title: \"Some API\"\n" +
                "basePath: \"/somepath\"\n" +
                "schemes:\n" +
                "- \"https\"\n" +
                "consumes:\n" +
                "- \"application/json\"\n" +
                "produces:\n" +
                "- \"application/json\"\n" +
                "paths:\n" +
                "  /somepath:\n" +
                "    get:\n" +
                "      description: \"my description\\n\"\n" +
                "      operationId: \"MyGet\"\n" +
                "      parameters: []\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          $ref: \"#/responses/Response\"\n" +
                "responses:\n" +
                "  Response:\n" +
                "    description: \"Response\"\n" +
                "    schema:\n" +
                "      type: \"object\"\n" +
                "      required:\n" +
                "      - \"Report\"\n" +
                "      properties:\n" +
                "        Report:\n" +
                "          type: \"string\"\n" +
                "      additionalProperties: false");
    }
}
