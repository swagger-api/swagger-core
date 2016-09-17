package io.swagger.functional.test;

import com.jayway.restassured.http.ContentType;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

/**
 * Created by rbolles on 2/16/16.
 * <p>
 * An functional integration test that runs during maven's integration-test phase,
 * uses RestAssured to define REST API tests, and Jetty's Maven plugin to serve a simple
 * sample app just prior to the integration-test phase starting.
 */
public class ApiListingResourceIT {
    private static final String EXPECTED_JSON = "{\"swagger\":\"2.0\",\"tags\":[{\"name\":\"widgets\"}],\"paths\":{\"/widgets/{widgetId}\":{\"get\":{\"tags\":[\"widgets\"],\"summary\":\"Find pet by ID\",\"description\":\"Returns a pet when ID <= 10.  ID > 10 or nonintegers will simulate API error conditions\",\"operationId\":\"getWidget\",\"consumes\":[\"application/json\"],\"produces\":[\"application/json\"],\"parameters\":[{\"name\":\"widgetId\",\"in\":\"path\",\"required\":true,\"type\":\"string\"}],\"responses\":{\"200\":{\"description\":\"Returns widget with matching id\"}}}}},\"definitions\":{\"Widget\":{\"type\":\"object\",\"properties\":{\"a\":{\"type\":\"string\"},\"b\":{\"type\":\"string\"},\"id\":{\"type\":\"string\"}}}}}";
    private static final String EXPECTED_YAML = "---\n" +
            "swagger: \"2.0\"\n" +
            "tags:\n" +
            "- name: \"widgets\"\n" +
            "paths:\n" +
            "  /widgets/{widgetId}:\n" +
            "    get:\n" +
            "      tags:\n" +
            "      - \"widgets\"\n" +
            "      summary: \"Find pet by ID\"\n" +
            "      description: \"Returns a pet when ID <= 10.  ID > 10 or nonintegers will simulate\\\n" +
            "        \\ API error conditions\"\n" +
            "      operationId: \"getWidget\"\n" +
            "      consumes:\n" +
            "      - \"application/json\"\n" +
            "      produces:\n" +
            "      - \"application/json\"\n" +
            "      parameters:\n" +
            "      - name: \"widgetId\"\n" +
            "        in: \"path\"\n" +
            "        required: true\n" +
            "        type: \"string\"\n" +
            "      responses:\n" +
            "        200:\n" +
            "          description: \"Returns widget with matching id\"\n" +
            "definitions:\n" +
            "  Widget:\n" +
            "    type: \"object\"\n" +
            "    properties:\n" +
            "      a:\n" +
            "        type: \"string\"\n" +
            "      b:\n" +
            "        type: \"string\"\n" +
            "      id:\n" +
            "        type: \"string\"\n";

    @Test
    public void testSwaggerJson() throws Exception {
        String actualBody = given()
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

        assertEquals(actualBody, EXPECTED_JSON);
    }

    @Test
    public void testSwaggerJsonUsingAcceptHeader() throws Exception {
        String actualBody = given()
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

        assertEquals(actualBody, EXPECTED_JSON);
    }

    @Test
    public void testSwaggerYaml() throws Exception {
        String actualBody = given()
                .log().all()
                .when()
                .get("/swagger.yaml")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .contentType("application/yaml")
                .extract().response().body().asString();

        assertEquals(actualBody, EXPECTED_YAML);
    }

    @Test
    public void testSwaggerYamlUsingAcceptHeader() throws Exception {
        String actualBody = given()
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

        assertEquals(actualBody, EXPECTED_YAML);
    }
}
