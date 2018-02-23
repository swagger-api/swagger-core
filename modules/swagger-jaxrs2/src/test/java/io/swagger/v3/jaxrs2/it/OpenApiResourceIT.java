package io.swagger.v3.jaxrs2.it;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.restassured.http.ContentType;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.annotations.AbstractAnnotationTest;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;

/**
 * <p>
 * An functional integration test that runs during maven's integration-test phase,
 * uses RestAssured to define REST API tests, and Jetty's Maven plugin to serve a simple
 * sample app just prior to the integration-test phase starting.
 */
public class OpenApiResourceIT extends AbstractAnnotationTest {
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
            "        200:\n" +
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
            "        200:\n" +
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
            "        200:\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                type: array\n" +
            "                items:\n" +
            "                  $ref: '#/components/schemas/Car_Summary'\n" +
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
            "        200:\n" +
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

    private static final int jettyPort = System.getProperties().containsKey("jetty.port") ? Integer.parseInt(System.getProperty("jetty.port")): -1;

    @BeforeMethod
    public void checkJetty() {
        if (jettyPort == -1) {
            throw new SkipException("Jetty not configured");
        }
    }

    @Test
    public void testSwaggerJson() throws Exception {

        String actualBody = given()
                .port(jettyPort)
                .log().all()
                .when()
                .get("/openapi.json")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response().body().asString();

        System.out.println(formatJson(actualBody));
        compareAsJson(formatJson(actualBody), EXPECTED_JSON);
    }

    @Test
    public void testSwaggerJsonUsingAcceptHeader() throws Exception {
        String actualBody = given()
                .port(jettyPort)
                .log().all()
                .accept(ContentType.JSON)
                .when()
                .get("/openapi")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response().body().asString();

        compareAsJson(formatJson(actualBody), EXPECTED_JSON);
    }

    @Test
    public void testSwaggerYaml() throws Exception {
        String actualBody = given()
                .port(jettyPort)
                .log().all()
                .when()
                .get("/openapi.yaml")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType("application/yaml")
                .extract().response().body().asString();

        System.out.println(formatYaml(actualBody));
        compareAsYaml(formatYaml(actualBody), EXPECTED_YAML);
    }

    @Test
    public void testSwaggerYamlUsingAcceptHeader() throws Exception {
        String actualBody = given()
                .port(jettyPort)
                .log().all()
                .accept("application/yaml")
                .when()
                .get("/openapi")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType("application/yaml")
                .extract().response().body().asString();

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
}
