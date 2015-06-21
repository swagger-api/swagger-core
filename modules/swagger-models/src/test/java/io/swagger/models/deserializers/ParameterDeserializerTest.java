package io.swagger.models.deserializers;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.CookieParameter;
import io.swagger.models.parameters.FormParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.parameters.RefParameter;

/**
 * @author <a href="mailto:dilip.gundu@wavemaker.com">Dilip Kumar</a>
 * @since 21/6/15
 */
public class ParameterDeserializerTest {

    private static final String TEST_PARAMETERS_DIR = "/parameters";

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void pathTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PARAMETERS_DIR + "/path.json");
        Parameter parameter = mapper.readValue(inputStream, Parameter.class);
        Assert.assertNotNull(parameter);
        Assert.assertTrue(parameter instanceof PathParameter);
    }

    @Test
    public void queryTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PARAMETERS_DIR + "/query.json");
        Parameter parameter = mapper.readValue(inputStream, Parameter.class);
        Assert.assertNotNull(parameter);
        Assert.assertTrue(parameter instanceof QueryParameter);
    }

    @Test
    public void bodyTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PARAMETERS_DIR + "/body.json");
        Parameter parameter = mapper.readValue(inputStream, Parameter.class);
        Assert.assertNotNull(parameter);
        Assert.assertTrue(parameter instanceof BodyParameter);
    }

    @Test
    public void formTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PARAMETERS_DIR + "/form.json");
        Parameter parameter = mapper.readValue(inputStream, Parameter.class);
        Assert.assertNotNull(parameter);
        Assert.assertTrue(parameter instanceof FormParameter);
    }

    @Test
    public void headerTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PARAMETERS_DIR + "/header.json");
        Parameter parameter = mapper.readValue(inputStream, Parameter.class);
        Assert.assertNotNull(parameter);
        Assert.assertTrue(parameter instanceof PathParameter);
    }

    @Test
    public void cookieTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PARAMETERS_DIR + "/cookie.json");
        Parameter parameter = mapper.readValue(inputStream, Parameter.class);
        Assert.assertNotNull(parameter);
        Assert.assertTrue(parameter instanceof CookieParameter);
    }

    @Test
    public void refParameterTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PARAMETERS_DIR + "/ref.json");
        Parameter parameter = mapper.readValue(inputStream, Parameter.class);
        Assert.assertNotNull(parameter);
        Assert.assertTrue(parameter instanceof RefParameter);
    }
}
