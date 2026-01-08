package io.swagger.v3.jaxrs2.annotations;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.JsonNode;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.jaxrs2.matchers.SerializationMatchers;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.MapperBuilder;
import tools.jackson.dataformat.yaml.YAMLFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import static org.testng.Assert.fail;

public abstract class AbstractAnnotationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAnnotationTest.class);

    public String readIntoYaml(final Class<?> cls) {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(cls);

        try {
            ObjectMapper mapper = Yaml.mapper(mapperBuilder -> mapperBuilder.changeDefaultPropertyInclusion(incl -> incl
                    .withContentInclusion(JsonInclude.Include.NON_NULL)
                    .withValueInclusion(JsonInclude.Include.NON_NULL)));
            // parse JSON
            JsonNode jsonNodeTree = mapper.readTree(mapper.writeValueAsString(openAPI));
            // return it as YAML
            return mapper.writeValueAsString(jsonNodeTree);
        } catch (Exception e) {
            return "Empty YAML";
        }
    }

    public void compareToYamlFile(final Class<?> cls, String source){

        final String file = source + cls.getSimpleName() + ".yaml";
            try {
            compareAsYaml(cls, getOpenAPIAsString(file));
        } catch (IOException e) {
            LOGGER.error("Failed to compare class {} with YAML resource {}", cls.getName(), file, e);
            fail();
        }
    }

    public void compareAsYaml(final Class<?> cls, final String yaml) throws IOException {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(cls);
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    public void compareAsYaml(final String actualYaml, final String expectedYaml) throws IOException {
        SerializationMatchers.assertEqualsToYaml(Yaml.mapper().readValue(actualYaml, OpenAPI.class), expectedYaml);
    }

    public void compareAsJson(final String actualJson, final String expectedJson) throws IOException {
        SerializationMatchers.assertEqualsToJson(Yaml.mapper().readValue(actualJson, OpenAPI.class), expectedJson);
    }

    protected String getOpenAPIAsString(final String file) throws IOException {
        InputStream in = null;
        try {
            in = getClass().getClassLoader().getResourceAsStream(file);
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public void compareAsYamlOAS31(final Class<?> cls, final String yaml) throws IOException {
        Reader reader = new Reader(new SwaggerConfiguration()
                .openAPI(new OpenAPI())
                .openAPI31(true));
        OpenAPI openAPI = reader.read(cls);
        SerializationMatchers.assertEqualsToYaml31(openAPI, yaml);
    }
}
