package io.swagger.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.dataformat.yaml.JacksonYAMLParseException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.swagger.matchers.SerializationMatchers;
import io.swagger.models.Swagger;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.LoaderOptions;

public class YamlSerializationTest {

    @Test
    public void testSerializeYAMLWithCustomFactory() throws Exception {
        // given
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setCodePointLimit(5 * 1024 * 1024);
        YAMLFactory yamlFactory = YAMLFactory.builder()
                .loaderOptions(loaderOptions)
                .build();
        final String yaml = ResourceUtils.loadClassResource(getClass(), "specFiles/petstore.yaml");

        // when
        Swagger swagger = ObjectMapperFactory.createYaml(yamlFactory).readValue(yaml, Swagger.class);

        // then
        SerializationMatchers.assertEqualsToYaml(swagger, yaml);
    }

    @Test(expectedExceptions = JacksonYAMLParseException.class)
    public void testSerializeYAMLWithCustomFactoryAndCodePointLimitReached() throws Exception {
        // given
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setCodePointLimit(1);
        YAMLFactory yamlFactory = YAMLFactory.builder()
                .loaderOptions(loaderOptions)
                .build();
        final String yaml = ResourceUtils.loadClassResource(getClass(), "specFiles/petstore.yaml");

        // when
        Swagger swagger = ObjectMapperFactory.createYaml(yamlFactory).readValue(yaml, Swagger.class);

        // then - Throw JacksonYAMLParseException
    }
}
