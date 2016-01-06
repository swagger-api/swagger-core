package io.swagger.models;

import io.swagger.TestUtils;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

@PrepareForTest({})
public class ModelImplTest extends PowerMockTestCase {

    private ModelImpl instance;

    Object[] propertiesAndValues;

    @BeforeMethod
    public void setUp() throws Exception {
        instance = new ModelImpl();
        propertiesAndValues = new Object[]{"additionalProperties", new ArrayProperty(), "description", "description",
                "discriminator", "discriminator", "example", new Object(), "format", "format", "isSimple", true, "name",
                "name", "properties", new HashMap<String, Property>(), "required", new ArrayList<String>(), "type",
                "type", "xml", new Xml(), "defaultValue", "defaultValue",};
    }

    @Test
    public void testClone() {
        // given
        propertiesAndValues = new Object[]{"additionalProperties", new ArrayProperty(), "description", "description",
                "discriminator", "discriminator", "example", new Object(), "isSimple", true, "name", "name",
                "properties", new HashMap<String, Property>(), "required", new ArrayList<String>(), "type", "type",
                "xml", new Xml(), "defaultValue", "defaultValue",};
        TestUtils.testClone(instance, propertiesAndValues);
    }

    @Test
    public void testGetProperties() {
        // then
        assertNull(instance.getProperties(), "New instance must have null as properties");
    }

    @Test
    public void testEnum() {
        // given
        List<String> _enum = new ArrayList<String>();
        assertEquals(instance._enum(_enum).getEnum(), _enum);
        instance.setEnum(null);
        String value = "value";

        // when
        instance._enum(value);

        // then
        assertTrue(instance.getEnum().contains(value), "The enums list must contain the new one");
    }

    @Test
    public void testConstructor() {
        // when
        instance = new ModelImpl();

        // then
        assertNull(instance.getDiscriminator(), "New instance must have null discriminator");
        assertNull(instance.getDescription(), "New instance must have null description");
        assertFalse(instance.isSimple(), "New instance must not be simple");
        assertNull(instance.getAdditionalProperties(), "New instance must have null additionalProperties");
        assertNull(instance.getExample(), "New instance must have null example");
        assertNull(instance.getDefaultValue(), "New instance must have null default value");
        assertNull(instance.getXml(), "New instance must have null Xml");
    }

    @Test
    public void testProperty() {
        // given
        String key = "key";
        Property property = new ArrayProperty();

        // when
        instance.property(key, property);

        // then
        assertEquals(instance.getProperties().get(key), property,
                "Must be able to retrieve the set value from the map");

        assertTrue(instance.required(key).getRequired().contains(key),
                "The set key must be contained in the required list");
    }

    @Test
    public void testSetRequired() {
        // given
        String required = "required";
        Property property = new ArrayProperty();
        instance.property(required, property);

        // when
        instance.setRequired(Arrays.asList(required));

        // then
        assertTrue(instance.getRequired().contains(required), "The set key must be contained in the required list");

    }

    @Test
    public void testAddProperty() {

        // given
        String badKey = "badKey";
        String key = "key";
        Property property = new ArrayProperty();
        instance.property(key, property);

        // when
        instance.addProperty(badKey, null);

        // then
        assertNull(instance.getProperties().get(badKey), "The bad key must not be added to the properties");

        // given
        instance.setRequired(Arrays.asList(key));

        // when
        instance.addProperty(key, property);
        assertEquals(instance.getProperties().get(key), property,
                "Must be able to retrieve the set value from the map");
    }
}
