package io.swagger.functional.test;

import static com.jayway.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.restassured.http.ContentType;
import io.swagger.util.Json;
import io.swagger.util.Yaml;

import java.io.IOException;

import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by rbolles on 2/16/16.
 * <p>
 * An functional integration test that runs during maven's integration-test phase,
 * uses RestAssured to define REST API tests, and Jetty's Maven plugin to serve a simple
 * sample app just prior to the integration-test phase starting.
 */
public class ApiListingResourceIT {

    private static final String EXPECTED_JSON = "{\n"
            + "  \"definitions\" : {\n"
            + "    \"Car\" : {\n"
            + "      \"properties\" : {\n"
            + "        \"color\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"manufacture\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"model\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"price\" : {\n"
            + "          \"format\" : \"int32\",\n"
            + "          \"type\" : \"integer\"\n"
            + "        },\n"
            + "        \"tires\" : {\n"
            + "          \"items\" : {\n"
            + "            \"$ref\" : \"#/definitions/Tire\"\n"
            + "          },\n"
            + "          \"type\" : \"array\"\n"
            + "        }\n"
            + "      },\n"
            + "      \"type\" : \"object\"\n"
            + "    },\n"
            + "    \"Car_Detail\" : {\n"
            + "      \"properties\" : {\n"
            + "        \"color\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"manufacture\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"model\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"tires\" : {\n"
            + "          \"items\" : {\n"
            + "            \"$ref\" : \"#/definitions/Tire_Detail\"\n"
            + "          },\n"
            + "          \"type\" : \"array\"\n"
            + "        }\n"
            + "      },\n"
            + "      \"type\" : \"object\"\n"
            + "    },\n"
            + "    \"Car_Summary\" : {\n"
            + "      \"properties\" : {\n"
            + "        \"color\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"manufacture\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"model\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        }\n"
            + "      },\n"
            + "      \"type\" : \"object\"\n"
            + "    },\n"
            + "    \"Car_Summary-or-Sale\" : {\n"
            + "      \"properties\" : {\n"
            + "        \"color\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"manufacture\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"model\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"price\" : {\n"
            + "          \"format\" : \"int32\",\n"
            + "          \"type\" : \"integer\"\n"
            + "        }\n"
            + "      },\n"
            + "      \"type\" : \"object\"\n"
            + "    },\n"
            + "    \"Tire\" : {\n"
            + "      \"properties\" : {\n"
            + "        \"brand\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"condition\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        }\n"
            + "      },\n"
            + "      \"type\" : \"object\"\n"
            + "    },\n"
            + "    \"Tire_Detail\" : {\n"
            + "      \"properties\" : {\n"
            + "        \"brand\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"condition\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        }\n"
            + "      },\n"
            + "      \"type\" : \"object\"\n"
            + "    },\n"
            + "    \"Widget\" : {\n"
            + "      \"properties\" : {\n"
            + "        \"a\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"b\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        },\n"
            + "        \"id\" : {\n"
            + "          \"type\" : \"string\"\n"
            + "        }\n"
            + "      },\n"
            + "      \"type\" : \"object\"\n"
            + "    }\n"
            + "  },\n"
            + "  \"paths\" : {\n"
            + "    \"/cars/all\" : {\n"
            + "      \"get\" : {\n"
            + "        \"consumes\" : [ \"application/json\" ],\n"
            + "        \"description\" : \"\",\n"
            + "        \"operationId\" : \"getAll\",\n"
            + "        \"parameters\" : [ ],\n"
            + "        \"produces\" : [ \"application/json\" ],\n"
            + "        \"responses\" : {\n"
            + "          \"200\" : {\n"
            + "            \"description\" : \"Return whole car\",\n"
            + "            \"schema\" : {\n"
            + "              \"items\" : {\n"
            + "                \"$ref\" : \"#/definitions/Car\"\n"
            + "              },\n"
            + "              \"type\" : \"array\"\n"
            + "            }\n"
            + "          }\n"
            + "        },\n"
            + "        \"summary\" : \"Return whole car\",\n"
            + "        \"tags\" : [ \"cars\" ]\n"
            + "      }\n"
            + "    },\n"
            + "    \"/cars/detail\" : {\n"
            + "      \"get\" : {\n"
            + "        \"consumes\" : [ \"application/json\" ],\n"
            + "        \"description\" : \"\",\n"
            + "        \"operationId\" : \"getDetails\",\n"
            + "        \"parameters\" : [ ],\n"
            + "        \"produces\" : [ \"application/json\" ],\n"
            + "        \"responses\" : {\n"
            + "          \"200\" : {\n"
            + "            \"description\" : \"Return car detail\",\n"
            + "            \"schema\" : {\n"
            + "              \"items\" : {\n"
            + "                \"$ref\" : \"#/definitions/Car_Detail\"\n"
            + "              },\n"
            + "              \"type\" : \"array\"\n"
            + "            }\n"
            + "          }\n"
            + "        },\n"
            + "        \"summary\" : \"Return car detail\",\n"
            + "        \"tags\" : [ \"cars\" ]\n"
            + "      }\n"
            + "    },\n"
            + "    \"/cars/sale\" : {\n"
            + "      \"get\" : {\n"
            + "        \"consumes\" : [ \"application/json\" ],\n"
            + "        \"description\" : \"\",\n"
            + "        \"operationId\" : \"getSaleSummaries\",\n"
            + "        \"parameters\" : [ ],\n"
            + "        \"produces\" : [ \"application/json\" ],\n"
            + "        \"responses\" : {\n"
            + "          \"200\" : {\n"
            + "            \"description\" : \"successful operation\",\n"
            + "            \"schema\" : {\n"
            + "              \"items\" : {\n"
            + "                \"$ref\" : \"#/definitions/Car_Summary-or-Sale\"\n"
            + "              },\n"
            + "              \"type\" : \"array\"\n"
            + "            }\n"
            + "          }\n"
            + "        },\n"
            + "        \"summary\" : \"Return car sale summary\",\n"
            + "        \"tags\" : [ \"cars\" ]\n"
            + "      }\n"
            + "    },\n"
            + "    \"/cars/summary\" : {\n"
            + "      \"get\" : {\n"
            + "        \"consumes\" : [ \"List\" ],\n"
            + "        \"description\" : \"\",\n"
            + "        \"operationId\" : \"getSummaries\",\n"
            + "        \"parameters\" : [ ],\n"
            + "        \"produces\" : [ \"application/json\" ],\n"
            + "        \"responses\" : {\n"
            + "          \"200\" : {\n"
            + "            \"description\" : \"successful operation\",\n"
            + "            \"schema\" : {\n"
            + "              \"$ref\" : \"#/definitions/Car_Summary\"\n"
            + "            }\n"
            + "          }\n"
            + "        },\n"
            + "        \"summary\" : \"Return car summaries\",\n"
            + "        \"tags\" : [ \"cars\" ]\n"
            + "      }\n"
            + "    },\n"
            + "    \"/widgets/{widgetId}\" : {\n"
            + "      \"get\" : {\n"
            + "        \"consumes\" : [ \"application/json\" ],\n"
            + "        \"description\" : \"Returns a pet when ID <= 10.  ID > 10 or nonintegers will simulate API error conditions\",\n"
            + "        \"operationId\" : \"getWidget\",\n"
            + "        \"parameters\" : [ {\n"
            + "          \"in\" : \"path\",\n"
            + "          \"name\" : \"widgetId\",\n"
            + "          \"required\" : true,\n"
            + "          \"type\" : \"string\"\n"
            + "        } ],\n"
            + "        \"produces\" : [ \"application/json\" ],\n"
            + "        \"responses\" : {\n"
            + "          \"200\" : {\n"
            + "            \"description\" : \"Returns widget with matching id\"\n"
            + "          }\n"
            + "        },\n"
            + "        \"summary\" : \"Find pet by ID\",\n"
            + "        \"tags\" : [ \"widgets\" ]\n"
            + "      }\n"
            + "    }\n"
            + "  },\n"
            + "  \"swagger\" : \"2.0\",\n"
            + "  \"tags\" : [ {\n"
            + "    \"name\" : \"cars\"\n"
            + "  }, {\n"
            + "    \"name\" : \"widgets\"\n"
            + "  } ]\n"
            + "}";
    private static final String EXPECTED_YAML = "---\n"
            + "definitions:\n"
            + "  Car:\n"
            + "    properties:\n"
            + "      color:\n"
            + "        type: \"string\"\n"
            + "      manufacture:\n"
            + "        type: \"string\"\n"
            + "      model:\n"
            + "        type: \"string\"\n"
            + "      price:\n"
            + "        format: \"int32\"\n"
            + "        type: \"integer\"\n"
            + "      tires:\n"
            + "        items:\n"
            + "          $ref: \"#/definitions/Tire\"\n"
            + "        type: \"array\"\n"
            + "    type: \"object\"\n"
            + "  Car_Detail:\n"
            + "    properties:\n"
            + "      color:\n"
            + "        type: \"string\"\n"
            + "      manufacture:\n"
            + "        type: \"string\"\n"
            + "      model:\n"
            + "        type: \"string\"\n"
            + "      tires:\n"
            + "        items:\n"
            + "          $ref: \"#/definitions/Tire_Detail\"\n"
            + "        type: \"array\"\n"
            + "    type: \"object\"\n"
            + "  Car_Summary:\n"
            + "    properties:\n"
            + "      color:\n"
            + "        type: \"string\"\n"
            + "      manufacture:\n"
            + "        type: \"string\"\n"
            + "      model:\n"
            + "        type: \"string\"\n"
            + "    type: \"object\"\n"
            + "  Car_Summary-or-Sale:\n"
            + "    properties:\n"
            + "      color:\n"
            + "        type: \"string\"\n"
            + "      manufacture:\n"
            + "        type: \"string\"\n"
            + "      model:\n"
            + "        type: \"string\"\n"
            + "      price:\n"
            + "        format: \"int32\"\n"
            + "        type: \"integer\"\n"
            + "    type: \"object\"\n"
            + "  Tire:\n"
            + "    properties:\n"
            + "      brand:\n"
            + "        type: \"string\"\n"
            + "      condition:\n"
            + "        type: \"string\"\n"
            + "    type: \"object\"\n"
            + "  Tire_Detail:\n"
            + "    properties:\n"
            + "      brand:\n"
            + "        type: \"string\"\n"
            + "      condition:\n"
            + "        type: \"string\"\n"
            + "    type: \"object\"\n"
            + "  Widget:\n"
            + "    properties:\n"
            + "      a:\n"
            + "        type: \"string\"\n"
            + "      b:\n"
            + "        type: \"string\"\n"
            + "      id:\n"
            + "        type: \"string\"\n"
            + "    type: \"object\"\n"
            + "paths:\n"
            + "  /cars/all:\n"
            + "    get:\n"
            + "      consumes:\n"
            + "      - \"application/json\"\n"
            + "      description: \"\"\n"
            + "      operationId: \"getAll\"\n"
            + "      parameters: []\n"
            + "      produces:\n"
            + "      - \"application/json\"\n"
            + "      responses:\n"
            + "        200:\n"
            + "          description: \"Return whole car\"\n"
            + "          schema:\n"
            + "            items:\n"
            + "              $ref: \"#/definitions/Car\"\n"
            + "            type: \"array\"\n"
            + "      summary: \"Return whole car\"\n"
            + "      tags:\n"
            + "      - \"cars\"\n"
            + "  /cars/detail:\n"
            + "    get:\n"
            + "      consumes:\n"
            + "      - \"application/json\"\n"
            + "      description: \"\"\n"
            + "      operationId: \"getDetails\"\n"
            + "      parameters: []\n"
            + "      produces:\n"
            + "      - \"application/json\"\n"
            + "      responses:\n"
            + "        200:\n"
            + "          description: \"Return car detail\"\n"
            + "          schema:\n"
            + "            items:\n"
            + "              $ref: \"#/definitions/Car_Detail\"\n"
            + "            type: \"array\"\n"
            + "      summary: \"Return car detail\"\n"
            + "      tags:\n"
            + "      - \"cars\"\n"
            + "  /cars/sale:\n"
            + "    get:\n"
            + "      consumes:\n"
            + "      - \"application/json\"\n"
            + "      description: \"\"\n"
            + "      operationId: \"getSaleSummaries\"\n"
            + "      parameters: []\n"
            + "      produces:\n"
            + "      - \"application/json\"\n"
            + "      responses:\n"
            + "        200:\n"
            + "          description: \"successful operation\"\n"
            + "          schema:\n"
            + "            items:\n"
            + "              $ref: \"#/definitions/Car_Summary-or-Sale\"\n"
            + "            type: \"array\"\n"
            + "      summary: \"Return car sale summary\"\n"
            + "      tags:\n"
            + "      - \"cars\"\n"
            + "  /cars/summary:\n"
            + "    get:\n"
            + "      consumes:\n"
            + "      - \"List\"\n"
            + "      description: \"\"\n"
            + "      operationId: \"getSummaries\"\n"
            + "      parameters: []\n"
            + "      produces:\n"
            + "      - \"application/json\"\n"
            + "      responses:\n"
            + "        200:\n"
            + "          description: \"successful operation\"\n"
            + "          schema:\n"
            + "            $ref: \"#/definitions/Car_Summary\"\n"
            + "      summary: \"Return car summaries\"\n"
            + "      tags:\n"
            + "      - \"cars\"\n"
            + "  /widgets/{widgetId}:\n"
            + "    get:\n"
            + "      consumes:\n"
            + "      - \"application/json\"\n"
            + "      description: \"Returns a pet when ID <= 10.  ID > 10 or nonintegers will simulate\\\n"
            + "        \\ API error conditions\"\n"
            + "      operationId: \"getWidget\"\n"
            + "      parameters:\n"
            + "      - in: \"path\"\n"
            + "        name: \"widgetId\"\n"
            + "        required: true\n"
            + "        type: \"string\"\n"
            + "      produces:\n"
            + "      - \"application/json\"\n"
            + "      responses:\n"
            + "        200:\n"
            + "          description: \"Returns widget with matching id\"\n"
            + "      summary: \"Find pet by ID\"\n"
            + "      tags:\n"
            + "      - \"widgets\"\n"
            + "swagger: \"2.0\"\n"
            + "tags:\n"
            + "- name: \"cars\"\n"
            + "- name: \"widgets\"\n";
    private static final int jettyPort = System.getProperties().containsKey("jetty.port") ? Integer
            .parseInt(System.getProperty("jetty.port")) : -1;

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
                .get("/swagger.json")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response().body().asString();

        System.out.println(EXPECTED_JSON);
        System.out.println(formatJson(actualBody));
        assertEquals(formatJson(actualBody), EXPECTED_JSON);
    }

    @Test
    public void testSwaggerJsonUsingAcceptHeader() throws Exception {
        String actualBody = given()
                .port(jettyPort)
                .log().all()
                .accept(ContentType.JSON)
                .when()
                .get("/swagger")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response().body().asString();

        assertEquals(formatJson(actualBody), EXPECTED_JSON);
    }

    @Test
    public void testSwaggerYaml() throws Exception {
        String actualBody = given()
                .port(jettyPort)
                .log().all()
                .when()
                .get("/swagger.yaml")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType("application/yaml")
                .extract().response().body().asString();

        assertEquals(formatYaml(actualBody), EXPECTED_YAML);
    }

    @Test
    public void testSwaggerYamlUsingAcceptHeader() throws Exception {
        String actualBody = given()
                .port(jettyPort)
                .log().all()
                .accept("application/yaml")
                .when()
                .get("/swagger")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType("application/yaml")
                .extract().response().body().asString();

        assertEquals(formatYaml(actualBody), EXPECTED_YAML);
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
