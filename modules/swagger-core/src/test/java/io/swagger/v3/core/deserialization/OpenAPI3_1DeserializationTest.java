package io.swagger.v3.core.deserialization;

import io.swagger.v3.core.matchers.SerializationMatchers;
import io.swagger.v3.core.util.ResourceUtils;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.core.util.Yaml31;
import io.swagger.v3.oas.models.OpenAPI;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class OpenAPI3_1DeserializationTest {

    @Test
    public void deserializePetstore3_1() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1.yaml");
        final OpenAPI swagger = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
    }

    @Test
    public void deserializePetstore3_1More() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/petstore-3.1_more.yaml");
        final OpenAPI swagger = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
    }


    @Test
    public void deserializePetstore3_0() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/petstore-3.0.yaml");
        final OpenAPI swagger = Yaml.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), null);
    }

    @Test
    public void deserializeChangelog3_1() throws IOException {

        final String jsonString = ResourceUtils.loadClassResource(getClass(), "specFiles/3.1.0/changelog-3.1.yaml");
        final OpenAPI swagger = Yaml31.mapper().readValue(jsonString, OpenAPI.class);
        assertNotNull(swagger);
        assertEquals(swagger.getInfo().getLicense().getIdentifier(), "test");
        Yaml31.prettyPrint(swagger);
        SerializationMatchers.assertEqualsToYaml31(swagger, jsonString);
    }
}
