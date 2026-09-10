package io.swagger.v3.core.serialization;

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.*;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.testng.annotations.Test;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.dataformat.yaml.JacksonYAMLParseException;
import tools.jackson.dataformat.yaml.YAMLFactory;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class OpenAPI31SerializationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAPI31SerializationTest.class);

    @Test
    public void testSerializePetstore() throws Exception {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1.yaml");
        final OpenAPI swagger = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
        SerializationMatchers.assertEqualsToYaml31(swagger, """
                openapi: 3.1.0
                info:
                  title: Swagger Petstore
                  license:
                    name: MIT
                    identifier: test
                  version: 1.0.0
                servers:
                - url: http://petstore.swagger.io/v1
                paths:
                  /pets:
                    get:
                      tags:
                      - pets
                      summary: List all pets
                      operationId: listPets
                      parameters:
                      - name: limit
                        in: query
                        description: How many items to return at one time (max 100)
                        required: false
                        schema:
                          type: integer
                          format: int32
                      responses:
                        "200":
                          description: An paged array of pets
                          headers:
                            x-next:
                              description: A link to the next page of responses
                              schema:
                                type: string
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Pets"
                        default:
                          description: unexpected error
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Error"
                    post:
                      tags:
                      - pets
                      summary: Create a pet
                      operationId: createPets
                      responses:
                        "201":
                          description: Null response
                        default:
                          description: unexpected error
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Error"
                  /pets/{petId}:
                    get:
                      tags:
                      - pets
                      summary: Info for a specific pet
                      operationId: showPetById
                      parameters:
                      - name: petId
                        in: path
                        description: The id of the pet to retrieve
                        required: true
                        schema:
                          type: string
                      responses:
                        "200":
                          description: Expected response to a valid request
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Pets"
                        default:
                          description: unexpected error
                          content:
                            application/json:
                              schema:
                                $ref: "#/components/schemas/Error"
                components:
                  schemas:
                    Pet:
                      required:
                      - id
                      - name
                      properties:
                        id:
                          type: integer
                          format: int64
                        name:
                          type:
                          - string
                          - integer
                        tag:
                          type: string
                    Pets:
                      type: array
                      items:
                        $ref: "#/components/schemas/Pet"
                    Error:
                      required:
                      - code
                      - message
                      properties:
                        code:
                          type: integer
                          format: int32
                        message:
                          type: string
                webhooks:
                  newPet:
                    post:
                      requestBody:
                        description: Information about a new pet in the system
                        content:
                          application/json:
                            schema:
                              $ref: "#/components/schemas/Pet"
                      responses:
                        "200":
                          description: Return a 200 status to indicate that the data was received
                            successfully\
                """);
        SerializationMatchers.assertEqualsToJson31(swagger, """
                {
                  "openapi" : "3.1.0",
                  "info" : {
                    "title" : "Swagger Petstore",
                    "license" : {
                      "name" : "MIT",
                      "identifier" : "test"
                    },
                    "version" : "1.0.0"
                  },
                  "servers" : [ {
                    "url" : "http://petstore.swagger.io/v1"
                  } ],
                  "paths" : {
                    "/pets" : {
                      "get" : {
                        "tags" : [ "pets" ],
                        "summary" : "List all pets",
                        "operationId" : "listPets",
                        "parameters" : [ {
                          "name" : "limit",
                          "in" : "query",
                          "description" : "How many items to return at one time (max 100)",
                          "required" : false,
                          "schema" : {
                            "type" : "integer",
                            "format" : "int32"
                          }
                        } ],
                        "responses" : {
                          "200" : {
                            "description" : "An paged array of pets",
                            "headers" : {
                              "x-next" : {
                                "description" : "A link to the next page of responses",
                                "schema" : {
                                  "type" : "string"
                                }
                              }
                            },
                            "content" : {
                              "application/json" : {
                                "schema" : {
                                  "$ref" : "#/components/schemas/Pets"
                                }
                              }
                            }
                          },
                          "default" : {
                            "description" : "unexpected error",
                            "content" : {
                              "application/json" : {
                                "schema" : {
                                  "$ref" : "#/components/schemas/Error"
                                }
                              }
                            }
                          }
                        }
                      },
                      "post" : {
                        "tags" : [ "pets" ],
                        "summary" : "Create a pet",
                        "operationId" : "createPets",
                        "responses" : {
                          "201" : {
                            "description" : "Null response"
                          },
                          "default" : {
                            "description" : "unexpected error",
                            "content" : {
                              "application/json" : {
                                "schema" : {
                                  "$ref" : "#/components/schemas/Error"
                                }
                              }
                            }
                          }
                        }
                      }
                    },
                    "/pets/{petId}" : {
                      "get" : {
                        "tags" : [ "pets" ],
                        "summary" : "Info for a specific pet",
                        "operationId" : "showPetById",
                        "parameters" : [ {
                          "name" : "petId",
                          "in" : "path",
                          "description" : "The id of the pet to retrieve",
                          "required" : true,
                          "schema" : {
                            "type" : "string"
                          }
                        } ],
                        "responses" : {
                          "200" : {
                            "description" : "Expected response to a valid request",
                            "content" : {
                              "application/json" : {
                                "schema" : {
                                  "$ref" : "#/components/schemas/Pets"
                                }
                              }
                            }
                          },
                          "default" : {
                            "description" : "unexpected error",
                            "content" : {
                              "application/json" : {
                                "schema" : {
                                  "$ref" : "#/components/schemas/Error"
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  },
                  "components" : {
                    "schemas" : {
                      "Pet" : {
                        "required" : [ "id", "name" ],
                        "properties" : {
                          "id" : {
                            "type" : "integer",
                            "format" : "int64"
                          },
                          "name" : {
                            "type" : ["string", "integer"]
                          },
                          "tag" : {
                            "type" : "string"
                          }
                        }
                      },
                      "Pets" : {
                        "type" : "array",
                        "items" : {
                          "$ref" : "#/components/schemas/Pet"
                        }
                      },
                      "Error" : {
                        "required" : [ "code", "message" ],
                        "properties" : {
                          "code" : {
                            "type" : "integer",
                            "format" : "int32"
                          },
                          "message" : {
                            "type" : "string"
                          }
                        }
                      }
                    }
                  },
                  "webhooks" : {
                    "newPet" : {
                      "post" : {
                        "requestBody" : {
                          "description" : "Information about a new pet in the system",
                          "content" : {
                            "application/json" : {
                              "schema" : {
                                "$ref" : "#/components/schemas/Pet"
                              }
                            }
                          }
                        },
                        "responses" : {
                          "200" : {
                            "description" : "Return a 200 status to indicate that the data was received successfully"
                          }
                        }
                      }
                    }
                  }
                }\
                """);

    }

    @Test
    public void testJSONSerializePetstoreWithCustomFactory() throws Exception {

        //given
        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1.json");
        JsonFactory jsonFactory = new JsonFactory();

        //when
        final OpenAPI swagger = ObjectMapperFactory.createJson31(jsonFactory).readValue(jsonString, OpenAPI.class);

        // then
        assertNotNull(swagger);
        SerializationMatchers.assertEqualsToJson31(swagger, jsonString);
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
        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                info:
                  title: Title test
                  description: This is a description for test
                  summary: Test Summary
                  termsOfService: https://test.term.of.services
                  contact:
                    name: Test Contact
                    url: https://test.contact.url
                    email: test@email.com
                  license:
                    name: test license
                    url: https://test.license.com
                    identifier: swagger
                  version: 1.0.0\
                """);

        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "info" : {
                    "title" : "Title test",
                    "description" : "This is a description for test",
                    "summary" : "Test Summary",
                    "termsOfService" : "https://test.term.of.services",
                    "contact" : {
                      "name" : "Test Contact",
                      "url" : "https://test.contact.url",
                      "email" : "test@email.com"
                    },
                    "license" : {
                      "name" : "test license",
                      "url" : "https://test.license.com",
                      "identifier" : "swagger"
                    },
                    "version" : "1.0.0"
                  }
                }\
                """);

        openAPI.setOpenapi("3.0.3");
        SerializationMatchers.assertEqualsToYaml(openAPI, """
                openapi: 3.0.3
                info:
                  title: Title test
                  description: This is a description for test
                  termsOfService: https://test.term.of.services
                  contact:
                    name: Test Contact
                    url: https://test.contact.url
                    email: test@email.com
                  license:
                    name: test license
                    url: https://test.license.com
                  version: 1.0.0\
                """);

        SerializationMatchers.assertEqualsToJson(openAPI, """
                {
                  "openapi" : "3.0.3",
                  "info" : {
                    "title" : "Title test",
                    "description" : "This is a description for test",
                    "termsOfService" : "https://test.term.of.services",
                    "contact" : {
                      "name" : "Test Contact",
                      "url" : "https://test.contact.url",
                      "email" : "test@email.com"
                    },
                    "license" : {
                      "name" : "test license",
                      "url" : "https://test.license.com"
                    },
                    "version" : "1.0.0"
                  }
                }\
                """);
    }

    @Test
    public void testWebHooksSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .addWebhooks("hook", new PathItem()
                        .description("test path hook")
                        .get(new Operation()
                                .operationId("testHookOperation")
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse().description("test response description")))));

        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                webhooks:
                  hook:
                    description: test path hook
                    get:
                      operationId: testHookOperation
                      responses:
                        "200":
                          description: test response description\
                """);

        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "webhooks" : {
                    "hook" : {
                      "description" : "test path hook",
                      "get" : {
                        "operationId" : "testHookOperation",
                        "responses" : {
                          "200" : {
                            "description" : "test response description"
                          }
                        }
                      }
                    }
                  }
                }\
                """);
        openAPI.setOpenapi("3.0.3");
        SerializationMatchers.assertEqualsToYaml(openAPI, "openapi: 3.0.3");
        SerializationMatchers.assertEqualsToJson(openAPI, """
                {
                  "openapi" : "3.0.3"
                }\
                """);

    }

    @Test
    public void testComponentPathItemsSerialization() {
        Schema schema = new StringSchema();
        schema.addType(schema.getType());
        OpenAPI openAPI = new OpenAPI().openapi("3.1.0").components(new Components()
                .addSchemas("stringTest", schema)
                .addPathItem("/pathTest", new PathItem()
                        .description("test path item")
                        .get(new Operation()
                                .operationId("testPathItem")
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse().description("response description")))))
                .addResponses("201", new ApiResponse()
                        .description("api response description"))
                .addParameters("param", new Parameter()
                        .in("query")
                        .description("parameter description")
                        .schema(schema))
                .addExamples("example", new Example()
                        .summary("example summary")
                        .value("This is an example/")
                        .description("example description"))
                .addRequestBodies("body", new RequestBody()
                        .content(new Content()
                                .addMediaType("application/json", new MediaType()
                                        .schema(new ObjectSchema()))))
                .addHeaders("test-head", new Header()
                        .description("test header description"))
                .addSecuritySchemes("basic", new SecurityScheme()
                        .in(SecurityScheme.In.HEADER)
                        .scheme("http")
                        .description("ref security description"))
                .addLinks("Link", new Link()
                        .operationRef("#/paths/~12.0~1repositories~1{username}/get"))
                .addCallbacks("TestCallback", new Callback().addPathItem("{$request.query.queryUrl}", new PathItem()
                        .description("test path item")
                        .post(new Operation()
                                .operationId("testPathItem")))));

        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                components:
                  schemas:
                    stringTest:
                      type: string
                  responses:
                    "201":
                      description: api response description
                  parameters:
                    param:
                      in: query
                      description: parameter description
                      schema:
                        type: string
                  examples:
                    example:
                      summary: example summary
                      description: example description
                      value: This is an example/
                  requestBodies:
                    body:
                      content:
                        application/json:
                          schema:\s
                            type: object
                  headers:
                    test-head:
                      description: test header description
                  securitySchemes:
                    basic:
                      description: ref security description
                      in: header
                      scheme: http
                  links:
                    Link:
                      operationRef: "#/paths/~12.0~1repositories~1{username}/get"
                  callbacks:
                    TestCallback:
                      '{$request.query.queryUrl}':
                        description: test path item
                        post:
                          operationId: testPathItem
                  pathItems:
                    /pathTest:
                      description: test path item
                      get:
                        operationId: testPathItem
                        responses:
                          "200":
                            description: response description\
                """);

        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "components" : {
                    "schemas" : {
                      "stringTest" : {
                        "type" : "string"
                      }
                    },
                    "responses" : {
                      "201" : {
                        "description" : "api response description"
                      }
                    },
                    "parameters" : {
                      "param" : {
                        "in" : "query",
                        "description" : "parameter description",
                        "schema" : {
                          "type" : "string"
                        }
                      }
                    },
                    "examples" : {
                      "example" : {
                        "summary" : "example summary",
                        "description" : "example description",
                        "value" : "This is an example/"
                      }
                    },
                    "requestBodies" : {
                      "body" : {
                        "content" : {
                          "application/json" : {
                            "schema" : {
                              "type" : "object"
                            }
                          }
                        }
                      }
                    },
                    "headers" : {
                      "test-head" : {
                        "description" : "test header description"
                      }
                    },
                    "securitySchemes" : {
                      "basic" : {
                        "description" : "ref security description",
                        "in" : "header",
                        "scheme" : "http"
                      }
                    },
                    "links" : {
                      "Link" : {
                        "operationRef" : "#/paths/~12.0~1repositories~1{username}/get"
                      }
                    },
                    "callbacks" : {
                      "TestCallback" : {
                        "{$request.query.queryUrl}" : {
                          "description" : "test path item",
                          "post" : {
                            "operationId" : "testPathItem"
                          }
                        }
                      }
                    },
                    "pathItems" : {
                      "/pathTest" : {
                        "description" : "test path item",
                        "get" : {
                          "operationId" : "testPathItem",
                          "responses" : {
                            "200" : {
                              "description" : "response description"
                            }
                          }
                        }
                      }
                    }
                  }
                }\
                """);

        openAPI.openapi("3.0.3");
        SerializationMatchers.assertEqualsToYaml(openAPI, """
                openapi: 3.0.3
                components:
                  schemas:
                    stringTest:
                      type: string
                  responses:
                    "201":
                      description: api response description
                  parameters:
                    param:
                      in: query
                      description: parameter description
                      schema:
                        type: string
                  examples:
                    example:
                      summary: example summary
                      description: example description
                      value: This is an example/
                  requestBodies:
                    body:
                      content:
                        application/json:
                          schema:
                            type: object
                  headers:
                    test-head:
                      description: test header description
                  securitySchemes:
                    basic:
                      description: ref security description
                      in: header
                      scheme: http
                  links:
                    Link:
                      operationRef: "#/paths/~12.0~1repositories~1{username}/get"
                  callbacks:
                    TestCallback:
                      '{$request.query.queryUrl}':
                        description: test path item
                        post:
                          operationId: testPathItem\
                """);

        SerializationMatchers.assertEqualsToJson(openAPI, """
                {
                  "openapi" : "3.0.3",
                  "components" : {
                    "schemas" : {
                      "stringTest" : {
                        "type" : "string"
                      }
                    },
                    "responses" : {
                      "201" : {
                        "description" : "api response description"
                      }
                    },
                    "parameters" : {
                      "param" : {
                        "in" : "query",
                        "description" : "parameter description",
                        "schema" : {
                          "type" : "string"
                        }
                      }
                    },
                    "examples" : {
                      "example" : {
                        "summary" : "example summary",
                        "description" : "example description",
                        "value" : "This is an example/"
                      }
                    },
                    "requestBodies" : {
                      "body" : {
                        "content" : {
                          "application/json" : {
                            "schema" : {
                              "type" : "object"
                            }
                          }
                        }
                      }
                    },
                    "headers" : {
                      "test-head" : {
                        "description" : "test header description"
                      }
                    },
                    "securitySchemes" : {
                      "basic" : {
                        "description" : "ref security description",
                        "in" : "header",
                        "scheme" : "http"
                      }
                    },
                    "links" : {
                      "Link" : {
                        "operationRef" : "#/paths/~12.0~1repositories~1{username}/get"
                      }
                    },
                    "callbacks" : {
                      "TestCallback" : {
                        "{$request.query.queryUrl}" : {
                          "description" : "test path item",
                          "post" : {
                            "operationId" : "testPathItem"
                          }
                        }
                      }
                    }
                  }
                }\
                """);
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

        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                components:
                  schemas:
                    pet:
                      properties:
                        name:
                          type: string
                        type:
                          type: string
                      discriminator:
                        propertyName: type
                        x-otherName: discriminationType
                      type: object\
                """);

        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "components" : {
                    "schemas" : {
                      "pet" : {
                        "properties" : {
                          "name" : {
                            "type" : "string"
                          },
                          "type" : {
                            "type" : "string"
                          }
                        },
                        "discriminator" : {
                          "propertyName" : "type",
                          "x-otherName" : "discriminationType"
                        },
                        "type" : "object"
                      }
                    }
                  }
                }\
                """);

        openAPI.openapi("3.0.3");

        SerializationMatchers.assertEqualsToYaml(openAPI, """
                openapi: 3.0.3
                components:
                  schemas:
                    pet:
                      properties:
                        name:
                          type: string
                        type:
                          type: string
                      discriminator:
                        propertyName: type
                      type: object\
                """);

        SerializationMatchers.assertEqualsToJson(openAPI, """
                {
                  "openapi" : "3.0.3",
                  "components" : {
                    "schemas" : {
                      "pet" : {
                        "properties" : {
                          "name" : {
                            "type" : "string"
                          },
                          "type" : {
                            "type" : "string"
                          }
                        },
                        "discriminator" : {
                          "propertyName" : "type"
                        },
                        "type" : "object"
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testPathItemsRefSerialization() {
        OpenAPI openAPI = new OpenAPI().openapi("3.1.0")
                .path("/pathTest", new PathItem()
                        .$ref("#/components/pathItems/pathTest")
                        .description("This is a ref path item")
                        .summary("ref path item")
                )
                .components(new Components()
                        .addPathItem("pathTest", new PathItem()
                                .description("test path item")
                                .get(new Operation()
                                        .operationId("testPathItem")
                                        .responses(new ApiResponses()
                                                .addApiResponse("200", new ApiResponse().description("response description"))))));

        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                paths:
                  /pathTest:
                    $ref: "#/components/pathItems/pathTest"
                    description: This is a ref path item
                    summary: ref path item
                components:
                  pathItems:
                    pathTest:
                      description: test path item
                      get:
                        operationId: testPathItem
                        responses:
                          "200":
                            description: response description\
                """);

        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "paths" : {
                    "/pathTest" : {
                      "summary" : "ref path item",
                      "description" : "This is a ref path item",
                      "$ref" : "#/components/pathItems/pathTest"
                    }
                  },
                  "components" : {
                    "pathItems" : {
                      "pathTest" : {
                        "description" : "test path item",
                        "get" : {
                          "operationId" : "testPathItem",
                          "responses" : {
                            "200" : {
                              "description" : "response description"
                            }
                          }
                        }
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testResponseRefSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .path("/test", new PathItem()
                        .description("test path item")
                        .get(new Operation()
                                .operationId("testPathItem")
                                .responses(new ApiResponses()
                                        .addApiResponse("200", new ApiResponse()
                                                .description("point to a $ref response")
                                                .$ref("#/components/responses/okResponse")))))
                .components(new Components()
                        .addResponses("okResponse", new ApiResponse().description("everything is good")));
        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                paths:
                  /test:
                    description: test path item
                    get:
                      operationId: testPathItem
                      responses:
                        "200":
                          description: point to a $ref response
                          $ref: "#/components/responses/okResponse"
                components:
                  responses:
                    okResponse:
                      description: everything is good\
                """);

        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "paths" : {
                    "/test" : {
                      "description" : "test path item",
                      "get" : {
                        "operationId" : "testPathItem",
                        "responses" : {
                          "200" : {
                            "description" : "point to a $ref response",
                            "$ref" : "#/components/responses/okResponse"
                          }
                        }
                      }
                    }
                  },
                  "components" : {
                    "responses" : {
                      "okResponse" : {
                        "description" : "everything is good"
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testParameterRefSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .components(new Components()
                        .addParameters("testParameter", new Parameter()
                                .in("query")))
                .path("/test", new PathItem()
                        .description("test path item")
                        .get(new Operation()
                                .operationId("testPathItem")
                                .addParametersItem(new Parameter()
                                        .$ref("#/components/parameters/testParameter")
                                        .description("test parameter"))));

        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                paths:
                  /test:
                    description: test path item
                    get:
                      operationId: testPathItem
                      parameters:
                      - description: test parameter
                        $ref: "#/components/parameters/testParameter"
                components:
                  parameters:
                    testParameter:
                      in: query\
                """);

        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "paths" : {
                    "/test" : {
                      "description" : "test path item",
                      "get" : {
                        "operationId" : "testPathItem",
                        "parameters" : [ {
                          "description" : "test parameter",
                          "$ref" : "#/components/parameters/testParameter"
                        } ]
                      }
                    }
                  },
                  "components" : {
                    "parameters" : {
                      "testParameter" : {
                        "in" : "query"
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testExampleRefSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .components(new Components()
                        .addExamples("testExample", new Example()
                                .value("Example on test")
                                .description("this is a example desc")
                                .summary("this is a summary test"))
                        .addSchemas("schema", new Schema().example(new Example()
                                .$ref("#/components/examples/testExample")
                                .description("ref description")
                                .summary("ref summary"))));

        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                components:
                  schemas:
                    schema:
                      example:
                        summary: ref summary
                        description: ref description
                        $ref: "#/components/examples/testExample"
                  examples:
                    testExample:
                      summary: this is a summary test
                      description: this is a example desc
                      value: Example on test\
                """);
        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "components" : {
                    "schemas" : {
                      "schema" : {
                        "example" : {
                          "summary" : "ref summary",
                          "description" : "ref description",
                          "$ref" : "#/components/examples/testExample"
                        }
                      }
                    },
                    "examples" : {
                      "testExample" : {
                        "summary" : "this is a summary test",
                        "description" : "this is a example desc",
                        "value" : "Example on test"
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testRequestBodyRefSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .path("/test", new PathItem()
                        .description("test path item")
                        .post(new Operation()
                                .operationId("testPathItem")
                                .requestBody(new RequestBody()
                                        .$ref("#/components/requestBodies/body")
                                        .description("ref request body"))))
                .components(new Components()
                        .addRequestBodies("body", new RequestBody()
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new ObjectSchema())))));
        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                paths:
                  /test:
                    description: test path item
                    post:
                      operationId: testPathItem
                      requestBody:
                        description: ref request body
                        $ref: "#/components/requestBodies/body"
                components:
                  requestBodies:
                    body:
                      content:
                        application/json:
                          schema:\s
                            type: object\
                """);
        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "paths" : {
                    "/test" : {
                      "description" : "test path item",
                      "post" : {
                        "operationId" : "testPathItem",
                        "requestBody" : {
                          "description" : "ref request body",
                          "$ref" : "#/components/requestBodies/body"
                        }
                      }
                    }
                  },
                  "components" : {
                    "requestBodies" : {
                      "body" : {
                        "content" : {
                          "application/json" : {
                            "schema" : {
                              "type" : "object"
                            }
                          }
                        }
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testHeaderRefSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .path("/test", new PathItem()
                        .description("test path item")
                        .post(new Operation()
                                .operationId("testPathItem")
                                .responses(new ApiResponses()
                                        .addApiResponse("default", new ApiResponse()
                                                .description("default response")
                                                .addHeaderObject("header", new Header()
                                                        .$ref("#/components/responses/okResponse")
                                                        .description("ref header description"))))
                        ))
                .components(new Components()
                        .addHeaders("test-head", new Header()
                                .description("test header description")));

        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                paths:
                  /test:
                    description: test path item
                    post:
                      operationId: testPathItem
                      responses:
                        default:
                          description: default response
                          headers:
                            header:
                              description: ref header description
                              $ref: "#/components/responses/okResponse"
                components:
                  headers:
                    test-head:
                      description: test header description\
                """);
        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "paths" : {
                    "/test" : {
                      "description" : "test path item",
                      "post" : {
                        "operationId" : "testPathItem",
                        "responses" : {
                          "default" : {
                            "description" : "default response",
                            "headers" : {
                              "header" : {
                                "description" : "ref header description",
                                "$ref" : "#/components/responses/okResponse"
                              }
                            }
                          }
                        }
                      }
                    }
                  },
                  "components" : {
                    "headers" : {
                      "test-head" : {
                        "description" : "test header description"
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testSecuritySchemeRefSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .components(new Components().addSecuritySchemes("basic", new SecurityScheme()
                        .$ref("https://external.site.com/#components/securitySchemes/basic")
                        .description("ref security description")));

        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                components:
                  securitySchemes:
                    basic:
                      description: ref security description
                      $ref: https://external.site.com/#components/securitySchemes/basic\
                """);
        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "components" : {
                    "securitySchemes" : {
                      "basic" : {
                        "description" : "ref security description",
                        "$ref" : "https://external.site.com/#components/securitySchemes/basic"
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testLinkRefSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .path("/test", new PathItem()
                        .description("test path item")
                        .post(new Operation()
                                .operationId("testPathItem")
                                .responses(new ApiResponses()
                                        .addApiResponse("default", new ApiResponse()
                                                .description("default response")
                                                .addLink("link", new Link()
                                                        .$ref("#/components/links/Link")
                                                        .description("ref link description"))))))
                .components(new Components().addLinks("Link", new Link()
                        .operationRef("#/paths/~12.0~1repositories~1{username}/get")));

        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                paths:
                  /test:
                    description: test path item
                    post:
                      operationId: testPathItem
                      responses:
                        default:
                          description: default response
                          links:
                            link:
                              description: ref link description
                              $ref: "#/components/links/Link"
                components:
                  links:
                    Link:
                      operationRef: "#/paths/~12.0~1repositories~1{username}/get"\
                """);
        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "paths" : {
                    "/test" : {
                      "description" : "test path item",
                      "post" : {
                        "operationId" : "testPathItem",
                        "responses" : {
                          "default" : {
                            "description" : "default response",
                            "links" : {
                              "link" : {
                                "description" : "ref link description",
                                "$ref" : "#/components/links/Link"
                              }
                            }
                          }
                        }
                      }
                    }
                  },
                  "components" : {
                    "links" : {
                      "Link" : {
                        "operationRef" : "#/paths/~12.0~1repositories~1{username}/get"
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testCallRefSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .path("/test", new PathItem()
                        .description("test path item")
                        .post(new Operation()
                                .operationId("testPathItem")
                                .addCallback("callbackSample", new Callback()
                                        .$ref("#/components/callbacks/TestCallback"))))
                .components(new Components().addCallbacks("TestCallback", new Callback().addPathItem("{$request.query.queryUrl}", new PathItem()
                        .description("test path item")
                        .post(new Operation()
                                .operationId("testPathItem")))));

        SerializationMatchers.assertEqualsToYaml31(openAPI, """
                openapi: 3.1.0
                paths:
                  /test:
                    description: test path item
                    post:
                      operationId: testPathItem
                      callbacks:
                        callbackSample:
                          $ref: "#/components/callbacks/TestCallback"
                components:
                  callbacks:
                    TestCallback:
                      '{$request.query.queryUrl}':
                        description: test path item
                        post:
                          operationId: testPathItem\
                """);

        SerializationMatchers.assertEqualsToJson31(openAPI, """
                {
                  "openapi" : "3.1.0",
                  "paths" : {
                    "/test" : {
                      "description" : "test path item",
                      "post" : {
                        "operationId" : "testPathItem",
                        "callbacks" : {
                          "callbackSample" : {
                            "$ref" : "#/components/callbacks/TestCallback"
                          }
                        }
                      }
                    }
                  },
                  "components" : {
                    "callbacks" : {
                      "TestCallback" : {
                        "{$request.query.queryUrl}" : {
                          "description" : "test path item",
                          "post" : {
                            "operationId" : "testPathItem"
                          }
                        }
                      }
                    }
                  }
                }\
                """);
    }

    @Test
    public void testBooleanSchemaSerialization() {
        OpenAPI openAPI = new OpenAPI()
                .openapi("3.1.0")
                .components(new Components().addSchemas("test", new Schema().booleanSchemaValue(true)));

        LOGGER.debug("--------- root ----------");
        Json31.prettyPrint(openAPI);
        assertEquals(Json31.pretty(openAPI), withJacksonSystemLineSeparator("""
                {
                  "openapi" : "3.1.0",
                  "components" : {
                    "schemas" : {
                      "test" : true
                    }
                  }
                }\
                """));
        LOGGER.debug("--------- schema ----------");
        Json31.prettyPrint(openAPI.getComponents().getSchemas().get("test"));
        assertEquals(Json31.pretty(openAPI.getComponents().getSchemas().get("test")), "true");
        LOGGER.debug("--------- root YAML----------");
        Yaml31.prettyPrint(openAPI);
        assertEquals(Yaml31.pretty(openAPI), """
                openapi: 3.1.0
                components:
                  schemas:
                    test: true
                """);
        LOGGER.debug("--------- schema YAML ----------");
        Yaml31.prettyPrint(openAPI.getComponents().getSchemas().get("test"));
        assertEquals(Yaml31.pretty(openAPI.getComponents().getSchemas().get("test")), "true\n");
        LOGGER.debug("--------- root 3.0 ----------");
        Json.prettyPrint(openAPI);
        assertEquals(Json.pretty(openAPI), withJacksonSystemLineSeparator("""
                {
                  "openapi" : "3.1.0",
                  "components" : {
                    "schemas" : {
                      "test" : { }
                    }
                  }
                }\
                """));
        LOGGER.debug("--------- schema 3.0 ----------");
        Json.prettyPrint(openAPI.getComponents().getSchemas().get("test"));
        assertEquals(Json.pretty(openAPI.getComponents().getSchemas().get("test")), "{ }");
        LOGGER.debug("--------- root YAML 3.0 ----------");
        Yaml.prettyPrint(openAPI);
        assertEquals(Yaml.pretty(openAPI), """
                openapi: 3.1.0
                components:
                  schemas:
                    test: {}
                """);
        LOGGER.debug("--------- schema YAML 3.0 ----------");
        Yaml.prettyPrint(openAPI.getComponents().getSchemas().get("test"));
        assertEquals(Yaml.pretty(openAPI.getComponents().getSchemas().get("test")), "{}\n");
    }

    @Test
    public void testBooleanAdditionalPropertiesSerialization() throws Exception {
        String expectedJson = """
                {
                  "openapi" : "3.1.0",
                  "components" : {
                    "schemas" : {
                      "test" : {
                        "type" : "object",
                        "additionalProperties" : true
                      }
                    }
                  }
                }\
                """;

        String expectedYaml = """
                openapi: 3.1.0
                components:
                  schemas:
                    test:
                      type: object
                      additionalProperties: true
                """;

        OpenAPI openAPI = Json31.mapper().readValue(expectedJson, OpenAPI.class);
        String ser = Json31.pretty(openAPI);
        assertEquals(ser, withJacksonSystemLineSeparator(expectedJson));
        assertEquals(openAPI.getComponents().getSchemas().get("test").getAdditionalProperties(), Boolean.TRUE);
        openAPI = Json.mapper().readValue(expectedJson, OpenAPI.class);
        ser = Json.pretty(openAPI);
        assertEquals(ser, withJacksonSystemLineSeparator(expectedJson));
        assertEquals(openAPI.getComponents().getSchemas().get("test").getAdditionalProperties(), Boolean.TRUE);

        openAPI = Yaml31.mapper().readValue(expectedYaml, OpenAPI.class);
        ser = Yaml31.pretty(openAPI);
        assertEquals(ser, expectedYaml);
        assertEquals(openAPI.getComponents().getSchemas().get("test").getAdditionalProperties(), Boolean.TRUE);
        openAPI = Yaml.mapper().readValue(expectedYaml, OpenAPI.class);
        ser = Yaml.pretty(openAPI);
        assertEquals(ser, expectedYaml);
        assertEquals(openAPI.getComponents().getSchemas().get("test").getAdditionalProperties(), Boolean.TRUE);

        expectedJson = """
                {
                  "openapi" : "3.0.0",
                  "components" : {
                    "schemas" : {
                      "test" : {
                        "type" : "object",
                        "additionalProperties" : true
                      }
                    }
                  }
                }\
                """;

        expectedYaml = """
                openapi: 3.0.0
                components:
                  schemas:
                    test:
                      type: object
                      additionalProperties: true
                """;

        openAPI = Json31.mapper().readValue(expectedJson, OpenAPI.class);
        ser = Json31.pretty(openAPI);
        assertEquals(ser, withJacksonSystemLineSeparator(expectedJson));
        assertEquals(openAPI.getComponents().getSchemas().get("test").getAdditionalProperties(), Boolean.TRUE);
        openAPI = Json.mapper().readValue(expectedJson, OpenAPI.class);
        ser = Json.pretty(openAPI);
        assertEquals(ser, withJacksonSystemLineSeparator(expectedJson));
        assertEquals(openAPI.getComponents().getSchemas().get("test").getAdditionalProperties(), Boolean.TRUE);

        openAPI = Yaml31.mapper().readValue(expectedYaml, OpenAPI.class);
        ser = Yaml31.pretty(openAPI);
        assertEquals(ser, expectedYaml);
        assertEquals(openAPI.getComponents().getSchemas().get("test").getAdditionalProperties(), Boolean.TRUE);
        openAPI = Yaml.mapper().readValue(expectedYaml, OpenAPI.class);
        ser = Yaml.pretty(openAPI);
        assertEquals(ser, expectedYaml);
        assertEquals(openAPI.getComponents().getSchemas().get("test").getAdditionalProperties(), Boolean.TRUE);
    }

    @Test(expectedExceptions = JacksonYAMLParseException.class)
    public void testSerializeYAML31WithCustomFactoryAndCodePointLimitReached() throws Exception {
        // given
        LoadSettings loadSettings = LoadSettings.builder()
                .setCodePointLimit(1)
                .build();
        YAMLFactory yamlFactory = YAMLFactory.builder()
                .loadSettings(loadSettings)
                .build();
        final String yaml = ResourceUtils.loadClassResource(getClass(), "specFiles/petstore-3.0.yaml");

        // when
        ObjectMapperFactory.createYaml31(yamlFactory).readValue(yaml, OpenAPI.class);

        // then - Throw JacksonYAMLParseException
    }

    private static String withJacksonSystemLineSeparator(String s) {
        return s.replace("\n", DefaultIndenter.SYS_LF);
    }
}
