package io.swagger.models.deserializers;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.DateProperty;
import io.swagger.models.properties.DateTimeProperty;
import io.swagger.models.properties.IntegerProperty;
import io.swagger.models.properties.MapProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.StringProperty;

/**
 * @author <a href="mailto:dilip.gundu@wavemaker.com">Dilip Kumar</a>
 * @since 21/6/15
 */
public class PropertyDeserializerTest {

    private static final String TEST_PROPERTIES_DIR = "/properties";

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void arrayTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PROPERTIES_DIR + "/array.json");
        Property property = mapper.readValue(inputStream, Property.class);
        Assert.assertNotNull(property);
        Assert.assertTrue(property instanceof ArrayProperty);
    }

    @Test
    public void dateTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PROPERTIES_DIR + "/date.json");
        Property property = mapper.readValue(inputStream, Property.class);
        Assert.assertNotNull(property);
        Assert.assertTrue(property instanceof DateProperty);
    }
    @Test
    public void dateTimeTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PROPERTIES_DIR + "/datetime.json");
        Property property = mapper.readValue(inputStream, Property.class);
        Assert.assertNotNull(property);
        Assert.assertTrue(property instanceof DateTimeProperty);
    }

    @Test
    public void integerTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PROPERTIES_DIR + "/integer.json");
        Property property = mapper.readValue(inputStream, Property.class);
        Assert.assertNotNull(property);
        Assert.assertTrue(property instanceof IntegerProperty);
    }

    @Test
    public void mapTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PROPERTIES_DIR + "/map.json");
        Property property = mapper.readValue(inputStream, Property.class);
        Assert.assertNotNull(property);
        Assert.assertTrue(property instanceof MapProperty);
    }

    @Test
    public void stringTest() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PROPERTIES_DIR + "/string.json");
        Property property = mapper.readValue(inputStream, Property.class);
        Assert.assertNotNull(property);
        Assert.assertTrue(property instanceof StringProperty);
    }

    @Test
    public void string2Test() throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(TEST_PROPERTIES_DIR + "/simple_string.json");
        Property property = mapper.readValue(inputStream, Property.class);
        Assert.assertNotNull(property);
        Assert.assertTrue(property instanceof StringProperty);
    }
}
