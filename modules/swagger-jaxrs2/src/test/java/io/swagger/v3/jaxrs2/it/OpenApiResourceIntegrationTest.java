package io.swagger.v3.jaxrs2.it;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.ContainerPerClassTestNgStrategy;
import org.glassfish.jersey.test.JerseyTestNg;
import org.glassfish.jersey.test.TestProperties;
import org.glassfish.jersey.test.spi.TestNgStrategy;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.SerializationFeature;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * <p>
 * An functional test that for the OpenApiResource.
 */
public class OpenApiResourceIntegrationTest extends JerseyTestNg
{
    private static final String EXPECTED_JSON = "{\n" +
            "    \"openapi\": \"3.0.1\",\n" +
            "    \"paths\": {\n" +
            "        \"/cars/all\": {\n" +
            "            \"get\": {\n" +
            "                \"tags\": [\n" +
            "                    \"cars\"\n" +
            "                ],\n" +
            "                \"description\": \"Return whole car\",\n" +
            "                \"operationId\": \"getAll\",\n" +
            "                \"responses\": {\n" +
            "                    \"200\": {\n" +
            "                        \"content\": {\n" +
            "                            \"application/json\": {\n" +
            "                                \"schema\": {\n" +
            "                                    \"type\": \"array\",\n" +
            "                                    \"items\": {\n" +
            "                                        \"$ref\": \"#/components/schemas/Car\"\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        \"/cars/summary\": {\n" +
            "            \"get\": {\n" +
            "                \"tags\": [\n" +
            "                    \"cars\"\n" +
            "                ],\n" +
            "                \"description\": \"Return car summaries\",\n" +
            "                \"operationId\": \"getSummaries\",\n" +
            "                \"responses\": {\n" +
            "                    \"200\": {\n" +
            "                        \"content\": {\n" +
            "                            \"application/json\": {\n" +
            "                                \"schema\": {\n" +
            "                                    \"type\": \"array\",\n" +
            "                                    \"items\": {\n" +
            "                                        \"$ref\": \"#/components/schemas/Car_Summary\"\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        \"/cars/detail\": {\n" +
            "            \"get\": {\n" +
            "                \"tags\": [\n" +
            "                    \"cars\"\n" +
            "                ],\n" +
            "                \"description\": \"Return car detail\",\n" +
            "                \"operationId\": \"getDetails\",\n" +
            "                \"responses\": {\n" +
            "                    \"200\": {\n" +
            "                        \"content\": {\n" +
            "                            \"application/json\": {\n" +
            "                                \"schema\": {\n" +
            "                                    \"type\": \"array\",\n" +
            "                                    \"items\": {\n" +
            "                                        \"$ref\": \"#/components/schemas/Car_Detail\"\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        \"/cars/sale\": {\n" +
            "            \"get\": {\n" +
            "                \"tags\": [\n" +
            "                    \"cars\"\n" +
            "                ],\n" +
            "                \"operationId\": \"getSaleSummaries\",\n" +
            "                \"responses\": {\n" +
            "                    \"default\": {\n" +
            "                        \"description\": \"default response\",\n" +
            "                        \"content\": {\n" +
            "                            \"application/json\": {\n" +
            "                                \"schema\": {\n" +
            "                                    \"type\": \"array\",\n" +
            "                                    \"items\": {\n" +
            "                                        \"$ref\": \"#/components/schemas/Car_Summary-or-Sale\"\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        \"/files/upload\": {\n" +
            "            \"post\": {\n" +
            "                \"operationId\": \"uploadFile\",\n" +
            "                \"requestBody\": {\n" +
            "                    \"content\": {\n" +
            "                        \"multipart/form-data\": {\n" +
            "                            \"schema\": {\n" +
            "                                \"type\": \"object\",\n" +
            "                                \"properties\": {\n" +
            "                                    \"fileIdRenamed\": {\n" +
            "                                        \"type\": \"string\"\n" +
            "                                    },\n" +
            "                                    \"fileRenamed\": {\n" +
            "                                        \"type\": \"string\",\n" +
            "                                        \"format\": \"binary\"\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                },\n" +
            "                \"responses\": {\n" +
            "                    \"default\": {\n" +
            "                        \"description\": \"default response\",\n" +
            "                        \"content\": {\n" +
            "                            \"application/json\": {\n" +
            "                                \n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        \"/files/attach\": {\n" +
            "            \"put\": {\n" +
            "                \"operationId\": \"putFile\",\n" +
            "                \"requestBody\": {\n" +
            "                    \"content\": {\n" +
            "                        \"application/octet-stream\": {\n" +
            "                            \"schema\": {\n" +
            "                                \"type\": \"string\",\n" +
            "                                \"format\": \"binary\"\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                },\n" +
            "                \"responses\": {\n" +
            "                    \"default\": {\n" +
            "                        \"description\": \"default response\",\n" +
            "                        \"content\": {\n" +
            "                            \"application/json\": {\n" +
            "                                \n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        \"/users/add\": {\n" +
            "            \"post\": {\n" +
            "                \"operationId\": \"addUser\",\n" +
            "                \"requestBody\": {\n" +
            "                    \"content\": {\n" +
            "                        \"application/x-www-form-urlencoded\": {\n" +
            "                            \"schema\": {\n" +
            "                                \"type\": \"object\",\n" +
            "                                \"properties\": {\n" +
            "                                    \"id\": {\n" +
            "                                        \"type\": \"string\"\n" +
            "                                    },\n" +
            "                                    \"name\": {\n" +
            "                                        \"type\": \"string\"\n" +
            "                                    },\n" +
            "                                    \"gender\": {\n" +
            "                                        \"type\": \"string\"\n" +
            "                                    }\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                },\n" +
            "                \"responses\": {\n" +
            "                    \"default\": {\n" +
            "                        \"description\": \"default response\",\n" +
            "                        \"content\": {\n" +
            "                            \"application/json\": {\n" +
            "                                \n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        \"/widgets/{widgetId}\": {\n" +
            "            \"get\": {\n" +
            "                \"tags\": [\n" +
            "                    \"widgets\"\n" +
            "                ],\n" +
            "                \"summary\": \"Find pet by ID\",\n" +
            "                \"description\": \"Returns a pet when ID <= 10.  ID > 10 or nonintegers will simulate API error conditions\",\n" +
            "                \"operationId\": \"getWidget\",\n" +
            "                \"parameters\": [\n" +
            "                    {\n" +
            "                        \"name\": \"widgetId\",\n" +
            "                        \"in\": \"path\",\n" +
            "                        \"required\": true,\n" +
            "                        \"schema\": {\n" +
            "                            \"type\": \"string\"\n" +
            "                        }\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"responses\": {\n" +
            "                    \"200\": {\n" +
            "                        \"description\": \"Returns widget with matching id\",\n" +
            "                        \"content\": {\n" +
            "                            \"application/json\": {\n" +
            "                                \"schema\": {\n" +
            "                                    \"$ref\": \"#/components/schemas/Widget\"\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    },\n" +
            "    \"components\": {\n" +
            "        \"schemas\": {\n" +
            "            \"Tire_Detail\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                    \"condition\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"brand\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            \"Car\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                    \"model\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"tires\": {\n" +
            "                        \"type\": \"array\",\n" +
            "                        \"items\": {\n" +
            "                            \"$ref\": \"#/components/schemas/Tire\"\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"price\": {\n" +
            "                        \"type\": \"integer\",\n" +
            "                        \"format\": \"int32\"\n" +
            "                    },\n" +
            "                    \"color\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"manufacture\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            \"Car_Summary-or-Sale\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                    \"model\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"price\": {\n" +
            "                        \"type\": \"integer\",\n" +
            "                        \"format\": \"int32\"\n" +
            "                    },\n" +
            "                    \"color\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"manufacture\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            \"Car_Detail\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                    \"model\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"tires\": {\n" +
            "                        \"type\": \"array\",\n" +
            "                        \"items\": {\n" +
            "                            \"$ref\": \"#/components/schemas/Tire_Detail\"\n" +
            "                        }\n" +
            "                    },\n" +
            "                    \"color\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"manufacture\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            \"Widget\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                    \"a\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"b\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"id\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            \"Car_Summary\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                    \"model\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"color\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"manufacture\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            \"Tire\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                    \"condition\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    },\n" +
            "                    \"brand\": {\n" +
            "                        \"type\": \"string\"\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";
    private static final String EXPECTED_YAML = "openapi: 3.0.1\n" +
            "paths:\n" +
            "  /cars/all:\n" +
            "    get:\n" +
            "      tags:\n" +
            "      - cars\n" +
            "      description: Return whole car\n" +
            "      operationId: getAll\n" +
            "      responses:\n" +
            "        \"200\":\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                type: array\n" +
            "                items:\n" +
            "                  $ref: '#/components/schemas/Car'\n" +
            "  /cars/detail:\n" +
            "    get:\n" +
            "      tags:\n" +
            "      - cars\n" +
            "      description: Return car detail\n" +
            "      operationId: getDetails\n" +
            "      responses:\n" +
            "        \"200\":\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                type: array\n" +
            "                items:\n" +
            "                  $ref: '#/components/schemas/Car_Detail'\n" +
            "  /cars/sale:\n" +
            "    get:\n" +
            "      tags:\n" +
            "      - cars\n" +
            "      operationId: getSaleSummaries\n" +
            "      responses:\n" +
            "        default:\n" +
            "          description: default response\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                type: array\n" +
            "                items:\n" +
            "                  $ref: '#/components/schemas/Car_Summary-or-Sale'\n" +
            "  /cars/summary:\n" +
            "    get:\n" +
            "      tags:\n" +
            "      - cars\n" +
            "      description: Return car summaries\n" +
            "      operationId: getSummaries\n" +
            "      responses:\n" +
            "        \"200\":\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                type: array\n" +
            "                items:\n" +
            "                  $ref: '#/components/schemas/Car_Summary'\n" +
            "  /files/attach:\n" +
            "    put:\n" +
            "      operationId: putFile\n" +
            "      requestBody:\n" +
            "        content:\n" +
            "          application/octet-stream:\n" +
            "            schema:\n" +
            "              type: string\n" +
            "              format: binary\n" +
            "      responses:\n" +
            "        default:\n" +
            "          description: default response\n" +
            "          content:\n" +
            "            application/json: {}\n" +
            "  /files/upload:\n" +
            "    post:\n" +
            "      operationId: uploadFile\n" +
            "      requestBody:\n" +
            "        content:\n" +
            "          multipart/form-data:\n" +
            "            schema:\n" +
            "              type: object\n" +
            "              properties:\n" +
            "                fileIdRenamed:\n" +
            "                  type: string\n" +
            "                fileRenamed:\n" +
            "                  type: string\n" +
            "                  format: binary\n" +
            "      responses:\n" +
            "        default:\n" +
            "          description: default response\n" +
            "          content:\n" +
            "            application/json: {}\n" +
            "  /users/add:\n" +
            "    post:\n" +
            "      operationId: addUser\n" +
            "      requestBody:\n" +
            "        content:\n" +
            "          application/x-www-form-urlencoded:\n" +
            "            schema:\n" +
            "              type: object\n" +
            "              properties:\n" +
            "                gender:\n" +
            "                  type: string\n" +
            "                id:\n" +
            "                  type: string\n" +
            "                name:\n" +
            "                  type: string\n" +
            "      responses:\n" +
            "        default:\n" +
            "          description: default response\n" +
            "          content:\n" +
            "            application/json: {}\n" +
            "  /widgets/{widgetId}:\n" +
            "    get:\n" +
            "      tags:\n" +
            "      - widgets\n" +
            "      summary: Find pet by ID\n" +
            "      description: Returns a pet when ID <= 10.  ID > 10 or nonintegers will simulate\n" +
            "        API error conditions\n" +
            "      operationId: getWidget\n" +
            "      parameters:\n" +
            "      - name: widgetId\n" +
            "        in: path\n" +
            "        required: true\n" +
            "        schema:\n" +
            "          type: string\n" +
            "      responses:\n" +
            "        \"200\":\n" +
            "          description: Returns widget with matching id\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                $ref: '#/components/schemas/Widget'\n" +
            "components:\n" +
            "  schemas:\n" +
            "    Car:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        color:\n" +
            "          type: string\n" +
            "        manufacture:\n" +
            "          type: string\n" +
            "        model:\n" +
            "          type: string\n" +
            "        price:\n" +
            "          type: integer\n" +
            "          format: int32\n" +
            "        tires:\n" +
            "          type: array\n" +
            "          items:\n" +
            "            $ref: '#/components/schemas/Tire'\n" +
            "    Car_Detail:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        color:\n" +
            "          type: string\n" +
            "        manufacture:\n" +
            "          type: string\n" +
            "        model:\n" +
            "          type: string\n" +
            "        tires:\n" +
            "          type: array\n" +
            "          items:\n" +
            "            $ref: '#/components/schemas/Tire_Detail'\n" +
            "    Car_Summary:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        color:\n" +
            "          type: string\n" +
            "        manufacture:\n" +
            "          type: string\n" +
            "        model:\n" +
            "          type: string\n" +
            "    Car_Summary-or-Sale:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        color:\n" +
            "          type: string\n" +
            "        manufacture:\n" +
            "          type: string\n" +
            "        model:\n" +
            "          type: string\n" +
            "        price:\n" +
            "          type: integer\n" +
            "          format: int32\n" +
            "    Tire:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        brand:\n" +
            "          type: string\n" +
            "        condition:\n" +
            "          type: string\n" +
            "    Tire_Detail:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        brand:\n" +
            "          type: string\n" +
            "        condition:\n" +
            "          type: string\n" +
            "    Widget:\n" +
            "      type: object\n" +
            "      properties:\n" +
            "        a:\n" +
            "          type: string\n" +
            "        b:\n" +
            "          type: string\n" +
            "        id:\n" +
            "          type: string\n";

    
    @Override
    protected Application configure() {
        return new ResourceConfig().property("jersey.config.server.wadl.disableWadl", true)
                .property("openApi.configuration.prettyPrint", true).property(TestProperties.LOG_TRAFFIC, true)
                .property(TestProperties.DUMP_ENTITY, true).register(MultiPartFeature.class)
                .packages("io.swagger.v3.jaxrs2.it.resources");
    }

    @Override
    protected URI getBaseUri() {
        return URI.create(super.getBaseUri().toString());
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MultiPartFeature.class);
        config.register(LoggingFeature.class);
    }

    @Override
    protected TestNgStrategy configureStrategy() {
        return new ContainerPerClassTestNgStrategy();
    }

    @BeforeClass
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterClass
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testSwaggerJson() throws Exception {
        final Response response = target("openapi.json").request().get();

        assertEquals(response.getStatus(), 200);
        assertEquals(response.getMediaType(), MediaType.APPLICATION_JSON_TYPE);

        final String actualBody = response.readEntity(String.class);

        compareAsJson(formatJson(actualBody), EXPECTED_JSON);
    }

    @Test
    public void testSwaggerJsonUsingAcceptHeader() throws Exception {
        final Response response = target("openapi").request().accept(MediaType.APPLICATION_JSON_TYPE).get();

        assertEquals(response.getStatus(), 200);
        assertEquals(response.getMediaType(), MediaType.APPLICATION_JSON_TYPE);

        final String actualBody = response.readEntity(String.class);

        compareAsJson(formatJson(actualBody), EXPECTED_JSON);
    }

    @Test
    public void testSwaggerYaml() throws Exception {
        final Response response = target("openapi.yaml").request().get();

        assertEquals(response.getStatus(), 200);
        assertEquals(response.getMediaType().toString(), "application/yaml");

        final String actualBody = response.readEntity(String.class);

        compareAsYaml(formatYaml(actualBody), EXPECTED_YAML);
    }

    @Test
    public void testSwaggerYamlUsingAcceptHeader() throws Exception {
        final Response response = target("openapi.yaml").request().accept("application/yaml").get();

        assertEquals(response.getStatus(), 200);
        assertEquals(response.getMediaType().toString(), "application/yaml");

        final String actualBody = response.readEntity(String.class);

        compareAsYaml(formatYaml(actualBody), EXPECTED_YAML);
    }

    private String formatYaml(String source) throws IOException {
        return Yaml.mapper().configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(Yaml.mapper().readValue(source, Object.class));
    }

    private String formatJson(String source) throws IOException {
        return Json.mapper().configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(Json.mapper().readValue(source, Object.class));
    }

    private void compareAsYaml(final String actualYaml, final String expectedYaml) throws IOException {
        SerializationMatchers.assertEqualsToYaml(Yaml.mapper().readValue(actualYaml, OpenAPI.class), expectedYaml);
    }

    private void compareAsJson(final String actualJson, final String expectedJson) throws IOException {
        SerializationMatchers.assertEqualsToJson(Yaml.mapper().readValue(actualJson, OpenAPI.class), expectedJson);
    }
}
