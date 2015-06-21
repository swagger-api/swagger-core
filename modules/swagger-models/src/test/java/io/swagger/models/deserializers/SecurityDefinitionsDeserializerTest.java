package io.swagger.models.deserializers;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.BasicAuthDefinition;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.auth.SecuritySchemeDefinition;

/**
 * @author <a href="mailto:dilip.gundu@wavemaker.com">Dilip Kumar</a>
 * @since 21/6/15
 */
public class SecurityDefinitionsDeserializerTest {

    private static final String TEST_DEFINITIONS_DIR = "/security_auth";

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void basicTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_DEFINITIONS_DIR + "/basic.json");
        SecuritySchemeDefinition definition = mapper.readValue(inputStream, SecuritySchemeDefinition.class);
        Assert.assertNotNull(definition);
        Assert.assertTrue(definition instanceof BasicAuthDefinition);
    }

    @Test
    public void apiKeyTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_DEFINITIONS_DIR + "/api_key.json");
        SecuritySchemeDefinition definition = mapper.readValue(inputStream, SecuritySchemeDefinition.class);
        Assert.assertNotNull(definition);
        Assert.assertTrue(definition instanceof ApiKeyAuthDefinition);
    }

    @Test
    public void oauth2Test() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_DEFINITIONS_DIR + "/oauth2.json");
        SecuritySchemeDefinition definition = mapper.readValue(inputStream, SecuritySchemeDefinition.class);
        Assert.assertNotNull(definition);
        Assert.assertTrue(definition instanceof OAuth2Definition);
    }

}
