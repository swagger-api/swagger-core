package io.swagger.v3.jaxrs2.annotations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.oas.models.OpenAPI;

import java.io.IOException;

public abstract class AbstractAnnotationTest {
    public String readIntoYaml(Class<?> cls) {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(cls);

        try {
            Yaml.mapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
            // parse JSON
            JsonNode jsonNodeTree = Yaml.mapper().readTree(Yaml.mapper().writeValueAsString(openAPI));
            // return it as YAML
            return Yaml.mapper().writeValueAsString(jsonNodeTree);
        } catch (Exception e) {
            return "Empty YAML";
        }
    }

    public void compareAsYaml(Class<?> cls, String yaml) throws IOException {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(cls);
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    public void compareAsYaml(String actualYaml, String expectedYaml) throws IOException {
        SerializationMatchers.assertEqualsToYaml(Yaml.mapper().readValue(actualYaml, OpenAPI.class), expectedYaml);
    }
    public void compareAsJson(String actualJson, String expectedJson) throws IOException {
        SerializationMatchers.assertEqualsToJson(Yaml.mapper().readValue(actualJson, OpenAPI.class), expectedJson);
    }

}
