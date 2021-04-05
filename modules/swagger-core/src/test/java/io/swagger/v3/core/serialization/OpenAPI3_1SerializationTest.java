package io.swagger.v3.core.serialization;

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class OpenAPI3_1SerializationTest {

    @Test
    public void testSerializePetstore() throws Exception {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1.yaml");
        final OpenAPI swagger = Yaml.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
        Yaml.prettyPrint(swagger);
        Json.prettyPrint(swagger);

        SerializationMatchers.assertEqualsToYaml(swagger, "openapi: 3.1.0\n" +
                "info:\n" +
                "  title: Swagger Petstore\n" +
                "  license:\n" +
                "    name: MIT\n" +
                "    identifier: test\n" +
                "  version: 1.0.0\n" +
                "servers:\n" +
                "- url: http://petstore.swagger.io/v1\n" +
                "paths:\n" +
                "  /pets:\n" +
                "    get:\n" +
                "      tags:\n" +
                "      - pets\n" +
                "      summary: List all pets\n" +
                "      operationId: listPets\n" +
                "      parameters:\n" +
                "      - name: limit\n" +
                "        in: query\n" +
                "        description: How many items to return at one time (max 100)\n" +
                "        required: false\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: An paged array of pets\n" +
                "          headers:\n" +
                "            x-next:\n" +
                "              description: A link to the next page of responses\n" +
                "              schema:\n" +
                "                type: string\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Pets'\n" +
                "        default:\n" +
                "          description: unexpected error\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Error'\n" +
                "    post:\n" +
                "      tags:\n" +
                "      - pets\n" +
                "      summary: Create a pet\n" +
                "      operationId: createPets\n" +
                "      responses:\n" +
                "        \"201\":\n" +
                "          description: Null response\n" +
                "        default:\n" +
                "          description: unexpected error\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Error'\n" +
                "  /pets/{petId}:\n" +
                "    get:\n" +
                "      tags:\n" +
                "      - pets\n" +
                "      summary: Info for a specific pet\n" +
                "      operationId: showPetById\n" +
                "      parameters:\n" +
                "      - name: petId\n" +
                "        in: path\n" +
                "        description: The id of the pet to retrieve\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: string\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: Expected response to a valid request\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Pets'\n" +
                "        default:\n" +
                "          description: unexpected error\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/Error'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    Pet:\n" +
                "      required:\n" +
                "      - id\n" +
                "      - name\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        name:\n" +
                "          type: string\n" +
                "        tag:\n" +
                "          type: string\n" +
                "    Pets:\n" +
                "      type: array\n" +
                "      items:\n" +
                "        $ref: '#/components/schemas/Pet'\n" +
                "    Error:\n" +
                "      required:\n" +
                "      - code\n" +
                "      - message\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        code:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        message:\n" +
                "          type: string\n" +
                "webhooks:\n" +
                "  newPet:\n" +
                "    post:\n" +
                "      requestBody:\n" +
                "        description: Information about a new pet in the system\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/Pet'\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: Return a 200 status to indicate that the data was received\n" +
                "            successfully");
        SerializationMatchers.assertEqualsToJson(swagger, "{\n" +
                "  \"openapi\" : \"3.1.0\",\n" +
                "  \"info\" : {\n" +
                "    \"title\" : \"Swagger Petstore\",\n" +
                "    \"license\" : {\n" +
                "      \"name\" : \"MIT\",\n" +
                "      \"identifier\" : \"test\"\n" +
                "    },\n" +
                "    \"version\" : \"1.0.0\"\n" +
                "  },\n" +
                "  \"servers\" : [ {\n" +
                "    \"url\" : \"http://petstore.swagger.io/v1\"\n" +
                "  } ],\n" +
                "  \"paths\" : {\n" +
                "    \"/pets\" : {\n" +
                "      \"get\" : {\n" +
                "        \"tags\" : [ \"pets\" ],\n" +
                "        \"summary\" : \"List all pets\",\n" +
                "        \"operationId\" : \"listPets\",\n" +
                "        \"parameters\" : [ {\n" +
                "          \"name\" : \"limit\",\n" +
                "          \"in\" : \"query\",\n" +
                "          \"description\" : \"How many items to return at one time (max 100)\",\n" +
                "          \"required\" : false,\n" +
                "          \"schema\" : {\n" +
                "            \"type\" : \"integer\",\n" +
                "            \"format\" : \"int32\"\n" +
                "          }\n" +
                "        } ],\n" +
                "        \"responses\" : {\n" +
                "          \"200\" : {\n" +
                "            \"description\" : \"An paged array of pets\",\n" +
                "            \"headers\" : {\n" +
                "              \"x-next\" : {\n" +
                "                \"description\" : \"A link to the next page of responses\",\n" +
                "                \"schema\" : {\n" +
                "                  \"type\" : \"string\"\n" +
                "                }\n" +
                "              }\n" +
                "            },\n" +
                "            \"content\" : {\n" +
                "              \"application/json\" : {\n" +
                "                \"schema\" : {\n" +
                "                  \"$ref\" : \"#/components/schemas/Pets\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"default\" : {\n" +
                "            \"description\" : \"unexpected error\",\n" +
                "            \"content\" : {\n" +
                "              \"application/json\" : {\n" +
                "                \"schema\" : {\n" +
                "                  \"$ref\" : \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"post\" : {\n" +
                "        \"tags\" : [ \"pets\" ],\n" +
                "        \"summary\" : \"Create a pet\",\n" +
                "        \"operationId\" : \"createPets\",\n" +
                "        \"responses\" : {\n" +
                "          \"201\" : {\n" +
                "            \"description\" : \"Null response\"\n" +
                "          },\n" +
                "          \"default\" : {\n" +
                "            \"description\" : \"unexpected error\",\n" +
                "            \"content\" : {\n" +
                "              \"application/json\" : {\n" +
                "                \"schema\" : {\n" +
                "                  \"$ref\" : \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"/pets/{petId}\" : {\n" +
                "      \"get\" : {\n" +
                "        \"tags\" : [ \"pets\" ],\n" +
                "        \"summary\" : \"Info for a specific pet\",\n" +
                "        \"operationId\" : \"showPetById\",\n" +
                "        \"parameters\" : [ {\n" +
                "          \"name\" : \"petId\",\n" +
                "          \"in\" : \"path\",\n" +
                "          \"description\" : \"The id of the pet to retrieve\",\n" +
                "          \"required\" : true,\n" +
                "          \"schema\" : {\n" +
                "            \"type\" : \"string\"\n" +
                "          }\n" +
                "        } ],\n" +
                "        \"responses\" : {\n" +
                "          \"200\" : {\n" +
                "            \"description\" : \"Expected response to a valid request\",\n" +
                "            \"content\" : {\n" +
                "              \"application/json\" : {\n" +
                "                \"schema\" : {\n" +
                "                  \"$ref\" : \"#/components/schemas/Pets\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          },\n" +
                "          \"default\" : {\n" +
                "            \"description\" : \"unexpected error\",\n" +
                "            \"content\" : {\n" +
                "              \"application/json\" : {\n" +
                "                \"schema\" : {\n" +
                "                  \"$ref\" : \"#/components/schemas/Error\"\n" +
                "                }\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"components\" : {\n" +
                "    \"schemas\" : {\n" +
                "      \"Pet\" : {\n" +
                "        \"required\" : [ \"id\", \"name\" ],\n" +
                "        \"type\" : \"object\",\n" +
                "        \"properties\" : {\n" +
                "          \"id\" : {\n" +
                "            \"type\" : \"integer\",\n" +
                "            \"format\" : \"int64\"\n" +
                "          },\n" +
                "          \"name\" : {\n" +
                "            \"type\" : \"string\"\n" +
                "          },\n" +
                "          \"tag\" : {\n" +
                "            \"type\" : \"string\"\n" +
                "          }\n" +
                "        }\n" +
                "      },\n" +
                "      \"Pets\" : {\n" +
                "        \"type\" : \"array\",\n" +
                "        \"items\" : {\n" +
                "          \"$ref\" : \"#/components/schemas/Pet\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"Error\" : {\n" +
                "        \"required\" : [ \"code\", \"message\" ],\n" +
                "        \"type\" : \"object\",\n" +
                "        \"properties\" : {\n" +
                "          \"code\" : {\n" +
                "            \"type\" : \"integer\",\n" +
                "            \"format\" : \"int32\"\n" +
                "          },\n" +
                "          \"message\" : {\n" +
                "            \"type\" : \"string\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"webhooks\" : {\n" +
                "    \"newPet\" : {\n" +
                "      \"post\" : {\n" +
                "        \"requestBody\" : {\n" +
                "          \"description\" : \"Information about a new pet in the system\",\n" +
                "          \"content\" : {\n" +
                "            \"application/json\" : {\n" +
                "              \"schema\" : {\n" +
                "                \"$ref\" : \"#/components/schemas/Pet\"\n" +
                "              }\n" +
                "            }\n" +
                "          }\n" +
                "        },\n" +
                "        \"responses\" : {\n" +
                "          \"200\" : {\n" +
                "            \"description\" : \"Return a 200 status to indicate that the data was received successfully\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");

    }
}
