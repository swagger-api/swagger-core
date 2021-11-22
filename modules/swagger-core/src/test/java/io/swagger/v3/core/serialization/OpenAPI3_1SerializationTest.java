package io.swagger.v3.core.serialization;

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class OpenAPI3_1SerializationTest {

    @Test
    public void testSerializePetstore() throws Exception {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1.yaml");
        final OpenAPI swagger = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
        SerializationMatchers.assertEqualsToYaml31(swagger, "openapi: 3.1.0\n" +
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
                "      properties:\n" +
                "        id:\n" +
                "          type: integer\n" +
                "          format: int64\n" +
                "        name:\n" +
                "          type:\n" +
                "          - string\n" +
                "          - integer\n" +
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
        SerializationMatchers.assertEqualsToJson31(swagger, "{\n" +
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
                "        \"properties\" : {\n" +
                "          \"id\" : {\n" +
                "            \"type\" : \"integer\",\n" +
                "            \"format\" : \"int64\"\n" +
                "          },\n" +
                "          \"name\" : {\n" +
                "            \"type\" : [\"string\", \"integer\"]\n" +
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

    @Test
    public void testInfoSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .info(new Info()
                        .title("Title test")
                        .description("This is a description for test")
                        .summary("Test Summary")
                        .version("1.0.0")
                        .termsOfService("https://test.term.of.services")
                        .contact(new Contact()
                                .name("Test Contact")
                                .url("https://test.contact.url")
                                .email("test@email.com"))
                        .license(new License()
                                .name("test license")
                                .url("https://test.license.com")
                                .identifier("swagger")));
        SerializationMatchers.assertEqualsToYaml31(openAPI, "openapi: 3.1.0\n" +
                "info:\n" +
                "  title: Title test\n" +
                "  description: This is a description for test\n" +
                "  summary: Test Summary\n" +
                "  termsOfService: https://test.term.of.services\n" +
                "  contact:\n" +
                "    name: Test Contact\n" +
                "    url: https://test.contact.url\n" +
                "    email: test@email.com\n" +
                "  license:\n" +
                "    name: test license\n" +
                "    url: https://test.license.com\n" +
                "    identifier: swagger\n" +
                "  version: 1.0.0");

        SerializationMatchers.assertEqualsToJson31(openAPI, "{\n" +
                "  \"openapi\" : \"3.1.0\",\n" +
                "  \"info\" : {\n" +
                "    \"title\" : \"Title test\",\n" +
                "    \"description\" : \"This is a description for test\",\n" +
                "    \"summary\" : \"Test Summary\",\n" +
                "    \"termsOfService\" : \"https://test.term.of.services\",\n" +
                "    \"contact\" : {\n" +
                "      \"name\" : \"Test Contact\",\n" +
                "      \"url\" : \"https://test.contact.url\",\n" +
                "      \"email\" : \"test@email.com\"\n" +
                "    },\n" +
                "    \"license\" : {\n" +
                "      \"name\" : \"test license\",\n" +
                "      \"url\" : \"https://test.license.com\",\n" +
                "      \"identifier\" : \"swagger\"\n" +
                "    },\n" +
                "    \"version\" : \"1.0.0\"\n" +
                "  }\n" +
                "}");

        openAPI.setOpenapi("3.0.3");
        SerializationMatchers.assertEqualsToYaml(openAPI, "openapi: 3.0.3\n" +
                "info:\n" +
                "  title: Title test\n" +
                "  description: This is a description for test\n" +
                "  termsOfService: https://test.term.of.services\n" +
                "  contact:\n" +
                "    name: Test Contact\n" +
                "    url: https://test.contact.url\n" +
                "    email: test@email.com\n" +
                "  license:\n" +
                "    name: test license\n" +
                "    url: https://test.license.com\n" +
                "  version: 1.0.0");

        SerializationMatchers.assertEqualsToJson(openAPI, "{\n" +
                "  \"openapi\" : \"3.0.3\",\n" +
                "  \"info\" : {\n" +
                "    \"title\" : \"Title test\",\n" +
                "    \"description\" : \"This is a description for test\",\n" +
                "    \"termsOfService\" : \"https://test.term.of.services\",\n" +
                "    \"contact\" : {\n" +
                "      \"name\" : \"Test Contact\",\n" +
                "      \"url\" : \"https://test.contact.url\",\n" +
                "      \"email\" : \"test@email.com\"\n" +
                "    },\n" +
                "    \"license\" : {\n" +
                "      \"name\" : \"test license\",\n" +
                "      \"url\" : \"https://test.license.com\"\n" +
                "    },\n" +
                "    \"version\" : \"1.0.0\"\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void testOWebHooksSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .addWebhooks("hook", new PathItem()
                        .description("test path hook")
                        .get(new Operation()
                                .operationId("testHookOperation")
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse().description("test response description")))));

        SerializationMatchers.assertEqualsToYaml31(openAPI, "openapi: 3.1.0\n" +
                "webhooks:\n" +
                "  hook:\n" +
                "    description: test path hook\n" +
                "    get:\n" +
                "      operationId: testHookOperation\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: test response description");

        SerializationMatchers.assertEqualsToJson31(openAPI, "{\n" +
                "  \"openapi\" : \"3.1.0\",\n" +
                "  \"webhooks\" : {\n" +
                "    \"hook\" : {\n" +
                "      \"description\" : \"test path hook\",\n" +
                "      \"get\" : {\n" +
                "        \"operationId\" : \"testHookOperation\",\n" +
                "        \"responses\" : {\n" +
                "          \"200\" : {\n" +
                "            \"description\" : \"test response description\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");
        openAPI.setOpenapi("3.0.3");
        SerializationMatchers.assertEqualsToYaml(openAPI, "openapi: 3.0.3");
        SerializationMatchers.assertEqualsToJson(openAPI, "{\n" +
                "  \"openapi\" : \"3.0.3\"\n}");

    }

    @Test
    public void testComponentPathItemsSerialization() {
        Schema schema = new StringSchema();
        schema.addType(schema.getType());
        OpenAPI openAPI = new OpenAPI().openapi("3.1.0").components(new Components()
                .addSchemas("stringTest", schema)
                .addPathItems("/pathTest", new PathItem()
                        .description("test path item")
                        .get(new Operation()
                                .operationId("testPathItem")
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse().description("response description"))))));

        SerializationMatchers.assertEqualsToYaml31(openAPI, "openapi: 3.1.0\n" +
                "components:\n" +
                "  schemas:\n" +
                "    stringTest:\n" +
                "      type: string\n" +
                "  pathItems:\n" +
                "    /pathTest:\n" +
                "      description: test path item\n" +
                "      get:\n" +
                "        operationId: testPathItem\n" +
                "        responses:\n" +
                "          \"200\":\n" +
                "            description: response description");

        SerializationMatchers.assertEqualsToJson31(openAPI, "{\n" +
                "  \"openapi\" : \"3.1.0\",\n" +
                "  \"components\" : {\n" +
                "    \"schemas\" : {\n" +
                "      \"stringTest\" : {" +
                "        \"type\" : \"string\"" +
                "       }\n" +
                "    },\n" +
                "    \"pathItems\" : {\n" +
                "      \"/pathTest\" : {\n" +
                "        \"description\" : \"test path item\",\n" +
                "        \"get\" : {\n" +
                "          \"operationId\" : \"testPathItem\",\n" +
                "          \"responses\" : {\n" +
                "            \"200\" : {\n" +
                "              \"description\" : \"response description\"\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");

        openAPI.openapi("3.0.3");
        SerializationMatchers.assertEqualsToYaml(openAPI, "openapi: 3.0.3\n" +
                "components:\n" +
                "  schemas:\n" +
                "    stringTest:\n" +
                "      type: string\n");

        SerializationMatchers.assertEqualsToJson(openAPI, "{\n" +
                "  \"openapi\" : \"3.0.3\",\n" +
                "  \"components\" : {\n" +
                "    \"schemas\" : {\n" +
                "      \"stringTest\" : {" +
                "        \"type\" : \"string\"" +
                "       }\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void testDiscriminatorSerialization() {
        Schema<String> propertySchema1 = new StringSchema();
        propertySchema1.addType(propertySchema1.getType());

        Schema<String> propertySchema2 = new StringSchema();
        propertySchema2.addType(propertySchema2.getType());

        Discriminator discriminator = new Discriminator().propertyName("type");
        discriminator.addExtension("x-otherName", "discriminationType");

        Schema schema = new ObjectSchema()
                .addProperties("name", propertySchema1)
                .addProperties("type", propertySchema1)
                .discriminator(discriminator);

        schema.addType(schema.getType());
        OpenAPI openAPI = new OpenAPI().openapi("3.1.0").components(new Components()
                .addSchemas("pet", schema));

        SerializationMatchers.assertEqualsToYaml31(openAPI, "openapi: 3.1.0\n" +
                "components:\n" +
                "  schemas:\n" +
                "    pet:\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string\n" +
                "        type:\n" +
                "          type: string\n" +
                "      discriminator:\n" +
                "        propertyName: type\n" +
                "        x-otherName: discriminationType\n" +
                "      type: object");

        SerializationMatchers.assertEqualsToJson31(openAPI, "{\n" +
                "  \"openapi\" : \"3.1.0\",\n" +
                "  \"components\" : {\n" +
                "    \"schemas\" : {\n" +
                "      \"pet\" : {\n" +
                "        \"properties\" : {\n" +
                "          \"name\" : {\n" +
                "            \"type\" : \"string\"\n" +
                "          },\n" +
                "          \"type\" : {\n" +
                "            \"type\" : \"string\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"discriminator\" : {\n" +
                "          \"propertyName\" : \"type\",\n" +
                "          \"x-otherName\" : \"discriminationType\"\n" +
                "        },\n" +
                "        \"type\" : \"object\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");

        openAPI.openapi("3.0.3");

        SerializationMatchers.assertEqualsToYaml(openAPI, "openapi: 3.0.3\n" +
                "components:\n" +
                "  schemas:\n" +
                "    pet:\n" +
                "      properties:\n" +
                "        name:\n" +
                "          type: string\n" +
                "        type:\n" +
                "          type: string\n" +
                "      discriminator:\n" +
                "        propertyName: type\n" +
                "      type: object");

        SerializationMatchers.assertEqualsToJson(openAPI, "{\n" +
                "  \"openapi\" : \"3.0.3\",\n" +
                "  \"components\" : {\n" +
                "    \"schemas\" : {\n" +
                "      \"pet\" : {\n" +
                "        \"properties\" : {\n" +
                "          \"name\" : {\n" +
                "            \"type\" : \"string\"\n" +
                "          },\n" +
                "          \"type\" : {\n" +
                "            \"type\" : \"string\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"discriminator\" : {\n" +
                "          \"propertyName\" : \"type\"\n" +
                "        },\n" +
                "        \"type\" : \"object\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }
}
