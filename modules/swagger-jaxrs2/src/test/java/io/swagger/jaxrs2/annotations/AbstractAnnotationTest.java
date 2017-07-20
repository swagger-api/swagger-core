package io.swagger.jaxrs2.annotations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.jaxrs2.Reader;
import io.swagger.oas.models.OpenAPI;
import io.swagger.util.Yaml;

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
}
