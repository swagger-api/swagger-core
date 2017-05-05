package io.swagger;

import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Response;

import org.testng.annotations.Test;

import java.io.IOException;

public class ResponseExamplesTest {

    @Test(description = "it should create a response")
    public void createResponse() throws IOException {
        final Response response = new Response()
                .example("application/json", "{\"name\":\"Fred\",\"id\":123456\"}");
        final String json = "{\n" +
                "   \"examples\":{\n" +
                "      \"application/json\":\"{\\\"name\\\":\\\"Fred\\\",\\\"id\\\":123456\\\"}\"\n" +
                "   }\n" +
                "}";
        SerializationMatchers.assertEqualsToJson(response, json);
    }
}
