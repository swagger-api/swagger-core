package io.swagger.util;

import io.swagger.models.*;
import io.swagger.models.properties.BooleanProperty;
import io.swagger.models.properties.RefProperty;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import java.io.InputStream;

import static org.testng.Assert.assertEquals;


public class YamlTest {

    @Test
    public void testSerializeYaml() throws Exception {
        Swagger swagger = new Swagger();
        swagger.addConsumes("application/json");
        swagger.addProduces("application/json");
        swagger.path("/health", new Path().get(new Operation().response(200, new Response().schema(new RefProperty("health")))));
        swagger.addDefinition("health", new ModelImpl().property("isHealthy", new BooleanProperty()));

        final String actual = Yaml.pretty().writeValueAsString(swagger);

        final InputStream stream = YamlTest.class.getResourceAsStream("/expectedSerializedYaml.yaml");
        final String expected = IOUtils.toString(stream);

        assertEquals(actual, expected);
    }
}
